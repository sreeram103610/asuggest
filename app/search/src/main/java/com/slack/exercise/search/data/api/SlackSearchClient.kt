package com.slack.exercise.search.data.api

import io.reactivex.rxjava3.core.Single

/**
 * Interface to the backend API.
 */
interface SlackSearchClient {
  /**
   * Fetches users with name or username matching the [searchTerm].
   * Calling the API passing an empty [searchTerm] fetches the entire team directory.
   *
   * Returns a [Single] emitting a set of [User] returned by the API or
   * an empty set if no users are found.
   *
   * Operates on a background thread.
   */
  fun searchUsers(searchTerm: String): Single<List<User>>
}