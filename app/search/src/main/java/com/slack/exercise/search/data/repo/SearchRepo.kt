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

interface SearchRepo {
    fun searchUsers(key: String): Flow<RepoResult<Set<UserDto>, RepoErrorType>>
}

internal class DefaultSearchRepo @Inject constructor(private val client: SlackSearchClient, private val cache: SearchCache) : SearchRepo {
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
