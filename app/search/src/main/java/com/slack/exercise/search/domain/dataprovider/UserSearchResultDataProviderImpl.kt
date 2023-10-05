package com.slack.exercise.search.domain.dataprovider

import com.slack.exercise.search.domain.model.UserSearchResult
import com.slack.exercise.search.domain.usecase.DomainResult
import com.slack.exercise.search.domain.usecase.GetUsersUsecase
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [UserSearchResultDataProvider].
 */

class UserSearchResultDataProviderImpl @Inject constructor(
    private val getUsersUsecase: GetUsersUsecase
) : UserSearchResultDataProvider {

  /**
   * Returns a [Single] emitting a set of [UserSearchResult].
   */
  override fun fetchUsers(searchTerm: String): Flow<DomainResult<List<UserSearchResult>>> {
    return getUsersUsecase(searchTerm)
  }
}