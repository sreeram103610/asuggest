package com.slack.exercise.search.data.util

import com.slack.exercise.search.BuildConfig
import com.slack.exercise.search.data.model.UserDto
import io.github.reactivecircus.cache4k.Cache
import org.apache.commons.collections4.trie.PatriciaTrie
import kotlin.time.Duration.Companion.minutes

/**
 * Defines the operations for caching and retrieving user search results.
 */
sealed interface SearchCache {

    /**
     * Adds a user and the associated search term to the cache.
     *
     * @param user The user to be added to the cache.
     * @param searchTerm The search term associated with the user.
     */
    fun addUser(user: UserDto, searchTerm: String)

    /**
     * Retrieves users from the cache based on a prefix key.
     *
     * @param prefixKey The prefix key to search for.
     * @return A set of users matching the prefix key.
     */
    fun searchUsers(prefixKey: String): Set<UserDto>

    /**
     * Adds a list of users and the associated key to the cache.
     *
     * @param userDtoList The list of users to be added to the cache.
     * @param key The key associated with the list of users.
     */
    fun addUsers(userDtoList: List<UserDto>, key: String)
}

/**
 * Default implementation of [SearchCache], handling the storage and retrieval of users within an in-memory cache.
 */
object DefaultSearchCache : SearchCache {

    private val cache =
        Cache.Builder<UserDto, String>().maximumCacheSize(BuildConfig.MEMORY_CACHE_SIZE.toLong())
            .expireAfterWrite(BuildConfig.MEMORY_CACHE_DURATION_MINUTES.minutes)
            .build()
    private val searchTrie = PatriciaTrie<HashSet<UserDto>>()

    /**
     * Add a user to the cache and associates it with a specific search term.
     *
     * @param user The user to be cached.
     * @param searchTerm The search term to associate with the cached user.
     */
    override fun addUser(user: UserDto, searchTerm: String) {
        searchTrie.getOrElse(user.displayName.lowercase()) {
            hashSetOf(user)
        }.let {
            searchTrie[user.displayName.lowercase()] = it.apply { add(user) }
            searchTrie[user.username.lowercase()] = it.apply { add(user) }
            cache.put(user, searchTerm.lowercase())
        }
    }

    /**
     * Searches for users in the cache that match a given prefix key.
     *
     * @param prefixKey The prefix key to match against cached entries.
     * @return A set of users that match the search phrase.
     */
    override fun searchUsers(prefixKey: String): Set<UserDto> {
        // Check if entries in the cache exists and that you return from cache if the search is not
        // broader and if it is additive. eg: don't return entries for "fa" if the searchPhrase is "f".
        val (validEntries, invalidEntries) = searchTrie.prefixMap(prefixKey.lowercase()).values.flatten()
            .partition {
                cache.get(it).let { cachedSearchPhrase ->
                    cachedSearchPhrase != null && prefixKey.startsWith(cachedSearchPhrase)
                }
            }
        removeUsers(invalidEntries)
        return validEntries.toSet()
    }

    /**
     * Adds a list of users to the cache and associates them with a specific key.
     *
     * @param userDtoList The list of users to be cached.
     * @param key The key to associate with the cached users.
     */
    override fun addUsers(userDtoList: List<UserDto>, key: String) {
        userDtoList.forEach { addUser(it, key.lowercase()) }
    }

    /**
     * Removes a list of users from the cache.
     *
     * @param list The list of users to be removed.
     */
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
