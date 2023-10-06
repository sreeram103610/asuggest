package com.slack.exercise.search.domain.model

sealed class DomainResult<out T> {
    object Loading : DomainResult<Nothing>()
    data class Loaded<out T>(val data: T) : DomainResult<T>()
    data class Error(val type: DomainErrorType) : DomainResult<Nothing>()
}

interface DomainErrorType
object InternetError : DomainErrorType
object NoUsersFound : DomainErrorType
