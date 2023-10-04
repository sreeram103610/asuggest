package com.slack.exercise.search.data.api

import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.data.model.UserDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

/**
 * Interface to the backend API.
 */
interface SlackSearchClient {
  /**
   * Fetches users with name or username matching the [searchTerm].
   * Calling the API passing an empty [searchTerm] fetches the entire team directory.
   *
   * Returns a [Single] emitting a set of [UserDto] returned by the API or
   * an empty set if no users are found.
   *
   * Operates on a background thread.
   */
  suspend fun searchUsers(searchTerm: String): DataResult<List<UserDto>, ErrorResponse>
}