package com.slack.exercise.search.domain.dataprovider

import com.slack.exercise.search.domain.model.UserSearchResult
import com.slack.exercise.search.domain.model.DomainResult
import com.slack.exercise.search.domain.usecase.GetUsersUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [UserSearchResultDataProvider].
 */

class UserSearchResultDataProviderImpl @Inject constructor(
    private val getUsersUsecase: GetUsersUsecase
) : UserSearchResultDataProvider {

  /**
   * Returns a [Flow] emitting a list of [UserSearchResult] if successful.
   */
  override fun fetchUsers(searchTerm: String): Flow<DomainResult<List<UserSearchResult>>> {
    return getUsersUsecase(searchTerm)
  }
}