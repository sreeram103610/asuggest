package com.slack.exercise.search.data.repo

import com.slack.exercise.search.data.api.SlackSearchClient
import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.data.model.UserDto
import com.slack.exercise.search.data.repo.model.RepoErrorType
import com.slack.exercise.search.data.repo.model.RepoResult
import com.slack.exercise.search.data.util.SearchCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository for conducting user searches.
 */
interface SearchRepo {
    /**
     * Searches for users based on a key.
     *
     * @param key The key to search for.
     * @return A [Flow] of [RepoResult] containing the search results or errors.
     */
    fun searchUsers(key: String): Flow<RepoResult<Set<UserDto>, RepoErrorType>>
}

/**
 * Default implementation of [SearchRepo], utilizing a [SlackSearchClient] and [SearchCache].
 *
 * @param client The client for searching users.
 * @param cache The cache for storing and retrieving user search results.
 */
internal class DefaultSearchRepo @Inject constructor(
    private val client: SlackSearchClient,
    private val cache: SearchCache
) : SearchRepo {
    /**
     * Searches for users by key. Initially checks the cache, if not found, queries the client.
     * The results from the client are then stored in the cache for future queries.
     *
     * @param key The key to search for.
     * @return A [Flow] of [RepoResult] containing the search results or errors.
     */
    override fun searchUsers(key: String): Flow<RepoResult<Set<UserDto>, RepoErrorType>> {
        return flow {
            emit(
                cache.searchUsers(key).let {
                    if (it.isNotEmpty()) {
                        RepoResult.Available(it)
                    } else {
                        client.searchUsers(key).let {
                            when (it) {
                                is DataResult.Success -> it.data.let { userDtoList ->
                                    cache.addUsers(userDtoList, key)
                                    RepoResult.Available(userDtoList.toSet())
                                }

                                is DataResult.Error -> when (it.errorType) {
                                    is ErrorResponse.NetworkError -> RepoResult.NotAvailable(
                                        RepoErrorType.NetworkError
                                    )

                                    is ErrorResponse.ServerError -> RepoResult.NotAvailable(
                                        RepoErrorType.ServerError
                                    )

                                    is ErrorResponse.UnknownError -> RepoResult.NotAvailable(
                                        RepoErrorType.UnknownError
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}
