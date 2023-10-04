package com.slack.exercise.search.dataprovider

import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.model.UserSearchResult
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

/**
 * Provider of [UserSearchResult].
 * This interface abstracts the logic of searching for users through the API or other data sources.
 */
interface UserSearchResultDataProvider {

  /**
   * Returns a [Single] emitting a set of [UserSearchResult].
   */
  fun fetchUsers(searchTerm: String): Flow<DataResult<Set<UserSearchResult>, ErrorResponse>>  // TODO: Change to domain result
}