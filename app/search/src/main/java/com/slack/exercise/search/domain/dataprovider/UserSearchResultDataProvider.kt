package com.slack.exercise.search.domain.dataprovider

import com.slack.exercise.search.domain.model.DomainResult
import com.slack.exercise.search.domain.model.UserSearchResult
import kotlinx.coroutines.flow.Flow

/**
 * Handles the retrieval of [UserSearchResult], abstracting the underlying data sources and search logic.
 */
interface UserSearchResultDataProvider {

    /**
     * Fetches users matching the provided search term.
     *
     * @param searchTerm The term to base the search on.
     * @return A [Flow] emitting the search results.
     */
    fun fetchUsers(searchTerm: String): Flow<DomainResult<List<UserSearchResult>>>
}
