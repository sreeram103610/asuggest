package com.slack.exercise.search.data.repo

import com.slack.exercise.search.data.api.BlockedPhrasesFileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.collections4.trie.PatriciaTrie
import timber.log.Timber
import javax.inject.Inject

interface BlockedPhrasesRepo {
    fun phraseExists(userName: String): Boolean
    fun addPhrase(userName: String)
}

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

    override fun phraseExists(userName: String): Boolean {
        return searchTrie.containsValue(userName)
    }

    override fun addPhrase(userName: String) {
        searchTrie.put(userName, userName)
    }

    private fun addUsers(usersList: List<String>) {
        searchTrie.putAll(usersList.associateWith { it })
    }
}
