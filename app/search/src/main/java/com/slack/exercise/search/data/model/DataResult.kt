package com.slack.exercise.search.data.model

sealed class DataResult<out T, out E : ErrorResponse> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>()
    data class Error<out E : ErrorResponse>(val errorType: E) : DataResult<Nothing, E>()
}

sealed interface ErrorResponse {
    object NetworkError : ErrorResponse
    object ServerError : ErrorResponse
    object UnknownError : ErrorResponse
}
