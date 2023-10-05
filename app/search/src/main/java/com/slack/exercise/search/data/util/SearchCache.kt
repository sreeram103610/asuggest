package com.slack.exercise.search.data.util

import com.slack.exercise.search.data.model.UserDto
import io.github.reactivecircus.cache4k.Cache
import org.apache.commons.collections4.trie.PatriciaTrie
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

object SearchCache{

    private val cache = Cache.Builder<UserDto, Boolean>().maximumCacheSize(100).expireAfterWrite(10.minutes).build()    // TODO: Add them to buildconfig
    private val searchTrie = PatriciaTrie<MutableList<UserDto>>()

    fun addUser(userDto: UserDto) {
        searchTrie.getOrElse(userDto.displayName) {
            mutableListOf(userDto)
        }.let {
            searchTrie[userDto.displayName] = it.apply { add(userDto) }
            searchTrie[userDto.username] = it.apply { add(userDto) }
            cache.put(userDto, true)
        }
    }

    fun searchUsers(prefixKey: String) : Set<UserDto> {
        val (validEntries, invalidEntries) = searchTrie.prefixMap(prefixKey).values.flatten().partition { cache.get(it) != null }
        removeUsers(invalidEntries)
        return validEntries.toSet()
    }

    private fun removeUsers(list: List<UserDto>) = list.forEach {
        searchTrie.remove(it.displayName)
        searchTrie.remove(it.username)
    }

    fun addUsers(userDtoList: List<UserDto>) {
        userDtoList.forEach { addUser(it) }
    }
}