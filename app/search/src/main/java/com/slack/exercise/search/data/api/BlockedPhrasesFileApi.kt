package com.slack.exercise.search.data.api

import android.content.Context
import com.slack.exercise.search.R
import com.slack.exercise.search.data.util.FileUtil
import java.io.File
import javax.inject.Inject

interface BlockedPhrasesFileApi {

    suspend fun getUsers(): List<String>
    suspend fun addUser(user: List<String>)
}

internal class DefaultBlockedPhrasesFileApi @Inject constructor(private val context: Context) : BlockedPhrasesFileApi {

    private val file = File(context.filesDir, FILE_NAME)
    init {
        if (!file.exists() || file.length() == 0L) {
            file.createNewFile()
            FileUtil.copyRawResourceToFile(context, R.raw.denylist, file)
        }
    }
    override suspend fun getUsers(): List<String> {
        return FileUtil.readFromFile(file)
    }

    override suspend fun addUser(user: List<String>) {
        return FileUtil.writeToFile(file, user, true)
    }

    companion object {
        const val FILE_NAME = "denyfile.txt"
    }
}
