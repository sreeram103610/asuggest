package com.slack.exercise.search.domain.dataprovider

import com.slack.exercise.search.domain.model.DomainResult
import com.slack.exercise.search.domain.model.UserSearchResult
import com.slack.exercise.search.domain.usecase.GetUsersUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [UserSearchResultDataProvider].
 *
 *  @property getUsersUsecase The use case to handle the retrieval of users.
 */

class UserSearchResultDataProviderImpl @Inject constructor(
    private val getUsersUsecase: GetUsersUsecase
) : UserSearchResultDataProvider {

    /**
     * Invokes [getUsersUsecase] to fetch users based on the specified [searchTerm], and emits the results as a [Flow].
     *
     * @param searchTerm The term to search users by.
     * @return A [Flow] emitting the search results.
     */
    override fun fetchUsers(searchTerm: String): Flow<DomainResult<List<UserSearchResult>>> {
        return getUsersUsecase(searchTerm)
    }
}
