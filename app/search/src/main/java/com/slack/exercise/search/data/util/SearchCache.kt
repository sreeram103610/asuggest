package com.slack.exercise.search.data.util

import com.slack.exercise.search.data.model.UserDto
import io.github.reactivecircus.cache4k.Cache
import org.apache.commons.collections4.trie.PatriciaTrie
import kotlin.time.Duration.Companion.minutes

sealed interface SearchCache {
    fun addUser(user: UserDto, searchTerm: String)
    fun searchUsers(prefixKey: String): Set<UserDto>
    fun addUsers(userDtoList: List<UserDto>, key: String)
}

object DefaultSearchCache : SearchCache {

    private val cache =
        Cache.Builder<UserDto, String>().maximumCacheSize(100).expireAfterWrite(10.minutes)
            .build() // TODO: Add them to buildconfig
    private val searchTrie = PatriciaTrie<HashSet<UserDto>>()

    override fun addUser(userDto: UserDto, searchTerm: String) {
        searchTrie.getOrElse(userDto.displayName.lowercase()) {
            hashSetOf(userDto)
        }.let {
            searchTrie[userDto.displayName.lowercase()] = it.apply { add(userDto) }
            searchTrie[userDto.username.lowercase()] = it.apply { add(userDto) }
            cache.put(userDto, searchTerm.lowercase())
        }
    }

    override fun searchUsers(searchPhrase: String): Set<UserDto> {
        // Check if entries in the cache exists and that you return from cache if the search is not broader and if it is additive. eg: don't return entries for "fa" if the searchPhrase is "f".
        val (validEntries, invalidEntries) = searchTrie.prefixMap(searchPhrase.lowercase()).values.flatten()
            .partition {
                cache.get(it).let { cachedSearchPhrase ->
                    cachedSearchPhrase != null && searchPhrase.startsWith(cachedSearchPhrase)
                }
            }
        removeUsers(invalidEntries)
        return validEntries.toSet()
    }

    override fun addUsers(userDtoList: List<UserDto>, key: String) {
        userDtoList.forEach { addUser(it, key.lowercase()) }
    }

    private fun removeUsers(list: List<UserDto>) = list.forEach { userDto ->
        searchTrie[userDto.displayName.lowercase()]?.remove(userDto)
        searchTrie[userDto.username.lowercase()]?.remove(userDto)

        if (searchTrie[userDto.displayName.lowercase()]?.isEmpty() == true) {
            searchTrie.remove(userDto.displayName.lowercase())
        }

        if (searchTrie[userDto.username.lowercase()]?.isEmpty() == true) {
            searchTrie.remove(userDto.username.lowercase())
        }
        cache.invalidate(userDto)
    }
}
