package com.slack.exercise.search.data.repo.model

sealed class RepoResult<out T, out E : RepoErrorType> {
    data class Available<out T>(val repoData: T) : RepoResult<T, Nothing>()
    data class NotAvailable<out E : RepoErrorType>(val error: RepoErrorType) :
        RepoResult<Nothing, E>()
}

sealed interface RepoErrorType {
    object NetworkError : RepoErrorType
    object ServerError : RepoErrorType
    object UnknownError : RepoErrorType
}
