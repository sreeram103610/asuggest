package com.slack.exercise.search.data.repo

import com.slack.exercise.search.data.api.BlockedPhrasesFileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.collections4.trie.PatriciaTrie
import timber.log.Timber
import javax.inject.Inject

/**
 * Repository interface for managing blocked phrases.
 */
interface BlockedPhrasesRepo {

    /**
     * Checks if a phrase is blocked.
     *
     * @param userName The phrase to check.
     * @return `true` if the phrase is blocked, otherwise `false`.
     */
    fun phraseExists(userName: String): Boolean

    /**
     * Adds a phrase to the blocked list.
     *
     * @param userName The phrase to block.
     */
    fun addPhrase(userName: String)
}

/**
 * Default Repository implementation of [BlockedPhrasesRepo].
 *
 * @property blockedFile API for accessing the blocked phrases file.
 */
class DefaultBlockedPhrasesRepo @Inject constructor(
    scope: CoroutineScope,
    private val blockedFile: BlockedPhrasesFileApi
) : BlockedPhrasesRepo {

    private val searchTrie = PatriciaTrie<String>()

    init {
        scope.launch {
            try {
                addUsers(blockedFile.getUsers())
            } catch (e: Exception) {
                Timber.d("Unable to add Users to Trie. ${e.message}")
            }
        }
    }

    /**
     * Checks if a username is in the blocked phrases list.
     */
    override fun phraseExists(userName: String): Boolean {
        return searchTrie.containsValue(userName)
    }

    /**
     * Adds a username to the blocked phrases list.
     */
    override fun addPhrase(userName: String) {
        searchTrie.put(userName, userName)
    }

    /**
     * Adds multiple users to the blocked phrases list.
     *
     * @param usersList The list of usernames to block.
     */
    private fun addUsers(usersList: List<String>) {
        searchTrie.putAll(usersList.associateWith { it })
    }
}
