package com.slack.exercise.search.data.api

import android.content.Context
import com.slack.exercise.search.R
import com.slack.exercise.search.data.util.FileUtil
import java.io.File
import javax.inject.Inject

/**
 * A interface for managing blocked phrases from a file.
 * Provides functions to read and write users to a file containing blocked phrases.
 */
interface BlockedPhrasesFileApi {

    /**
     * Retrieves a list of users who are associated with blocked phrases.
     *
     * @return A List of user strings.
     */
    suspend fun getUsers(): List<String>

    /**
     * Adds a list of users to a file containing blocked phrases.
     *
     * @param user A list of user strings to be added to the blocked phrases file.
     */
    suspend fun addUser(user: List<String>)
}

/**
 * Default implementation of the [BlockedPhrasesFileApi] interface.
 * Manages the reading and writing of users to a file containing blocked phrases.
 *
 * @property context The context used to access file directories and resources.
 */
internal class DefaultBlockedPhrasesFileApi @Inject constructor(private val context: Context) :
    BlockedPhrasesFileApi {

    // The file that contains the list of users associated with blocked phrases.
    private val file = File(context.filesDir, FILE_NAME)

    init {
        // Ensures the file exists and is initialized with default blocked phrases if empty.
        if (!file.exists() || file.length() == 0L) {
            file.createNewFile()
            FileUtil.copyRawResourceToFile(context, R.raw.denylist, file)
        }
    }

    /**
     * Retrieves a list of users from the blocked phrases file.
     *
     * @return A List of user strings read from the file.
     */
    override suspend fun getUsers(): List<String> {
        return FileUtil.readFromFile(file)
    }

    /**
     * Writes a list of users to the blocked phrases file.
     *
     * @param user A list of user strings to be written to the file.
     */
    override suspend fun addUser(user: List<String>) {
        return FileUtil.writeToFile(file, user, true)
    }

    companion object {
        const val FILE_NAME = "denyfile.txt"
    }
}
