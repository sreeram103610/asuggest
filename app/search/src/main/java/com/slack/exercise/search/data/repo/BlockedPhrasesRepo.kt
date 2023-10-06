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
     * @param searchTerm The phrase to check.
     * @return `true` if the phrase is blocked, otherwise `false`.
     */
    fun phraseExists(searchTerm: String): Boolean

    /**
     * Adds a phrase to the blocked list.
     *
     * @param searchTerm The phrase to block.
     */
    fun addPhrase(searchTerm: String)
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

    private val searchTrie = PatriciaTrie<Boolean>()

    init {
        scope.launch {
            try {
                addPhrases(blockedFile.getUsers())
            } catch (e: Exception) {
                Timber.d("Unable to add Users to Trie. ${e.message}")
            }
        }
    }

    /**
     * Checks if a search term or it's prefixes are in the blocked phrases list.
     */
    override fun phraseExists(searchTerm: String): Boolean {
        searchTerm.indices
            .forEach { if(searchTrie.containsKey(searchTerm.substring(0, it + 1))) return true }
        return false
    }

    /**
     * Adds a searchTerm to the blocked phrases list.
     */
    override fun addPhrase(searchTerm: String) {
        searchTrie[searchTerm] = true
    }

    /**
     * Adds multiple terms to the blocked phrases list.
     *
     * @param phraseList The list of usernames to block.
     */
    private fun addPhrases(phraseList: List<String>) {
        phraseList.forEach { addPhrase(it) }
    }
}
