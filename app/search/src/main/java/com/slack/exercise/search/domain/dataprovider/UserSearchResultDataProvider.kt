package com.slack.exercise.search.domain.dataprovider

import com.slack.exercise.search.domain.model.UserSearchResult
import com.slack.exercise.search.domain.model.DomainResult
import kotlinx.coroutines.flow.Flow

/**
 * Provider of [UserSearchResult].
 * This interface abstracts the logic of searching for users through the API or other data sources.
 */
interface UserSearchResultDataProvider {

  /**
   * Returns a [FLow] emitting a list of [UserSearchResult] if successful.
   */
  fun fetchUsers(searchTerm: String): Flow<DomainResult<List<UserSearchResult>>>  // TODO: Change to domain result
}