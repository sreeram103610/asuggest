package com.slack.exercise.search.data.repo

import com.slack.exercise.search.data.api.SlackSearchClient
import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.data.repo.model.RepoErrorType
import com.slack.exercise.search.data.repo.model.RepoResult
import com.slack.exercise.search.model.UserSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

interface SearchRepo {
    fun searchUsers(key: String): Flow<RepoResult<Set<UserSearchResult>, RepoErrorType>>
}

internal class DefaultSearchRepo(private val client: SlackSearchClient) : SearchRepo {
    override fun searchUsers(key: String): Flow<RepoResult<Set<UserSearchResult>, RepoErrorType>> {
        return flow { emit(
            client.searchUsers(key).let {
            when(it) {
                is DataResult.Success -> RepoResult.Available(it.data.map { user -> UserSearchResult.from(user) }.toSet())
                is DataResult.Error -> when(it.errorType) {
                    is ErrorResponse.NetworkError -> RepoResult.NotAvailable(RepoErrorType.NetworkError)
                    is ErrorResponse.ServerError -> RepoResult.NotAvailable(RepoErrorType.ServerError)
                    is ErrorResponse.UnknownError -> RepoResult.NotAvailable(RepoErrorType.UnknownError)
                }
            }
        })
        }
    }

}