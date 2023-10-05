package com.slack.exercise.search.data.api

import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.data.model.UserDto

/**
 * Interface to the backend API.
 */
interface SlackSearchClient {
    /**
     * Fetches users with name or username matching the [searchTerm].
     * Calling the API passing an empty [searchTerm] fetches the entire team directory.
     *
     * Returns a [DataResult] emitting [DataResult.Success] with users or [DataResult.Error]
     * along with [ErrorResponse]
     */
    suspend fun searchUsers(searchTerm: String): DataResult<List<UserDto>, ErrorResponse>
}
