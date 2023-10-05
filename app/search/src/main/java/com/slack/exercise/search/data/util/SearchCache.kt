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
object DefaultSearchCache : SearchCache{

    private val cache = Cache.Builder<UserDto, String>().maximumCacheSize(100).expireAfterWrite(10.minutes).build()    // TODO: Add them to buildconfig
    private val searchTrie = PatriciaTrie<HashSet<UserDto>>()

    override fun addUser(userDto: UserDto, searchTerm: String) {
        searchTrie.getOrElse(userDto.displayName) {
            hashSetOf(userDto)
        }.let {
            searchTrie[userDto.displayName] = it.apply { add(userDto) }
            searchTrie[userDto.username] = it.apply { add(userDto) }
            cache.put(userDto, searchTerm)
        }
    }

    override fun searchUsers(prefixKey: String) : Set<UserDto> {
        // Check if entries in the cache exists and that the cached entry is not a subset of the searchTerm's entries.
        val (validEntries, invalidEntries) = searchTrie.prefixMap(prefixKey).values.flatten().partition { cache.get(it).let { searchPhrase -> searchPhrase != null && searchPhrase.length <= prefixKey.length }  }
        removeUsers(invalidEntries)
        return validEntries.toSet()
    }

    override fun addUsers(userDtoList: List<UserDto>, key: String) {
        userDtoList.forEach { addUser(it, key) }
    }

    private fun removeUsers(list: List<UserDto>) = list.forEach {
        searchTrie.remove(it.displayName)
        searchTrie.remove(it.username)
    }

}