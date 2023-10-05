package com.slack.exercise.search.ui.usersearch

import com.slack.exercise.search.domain.model.DomainResult
import com.slack.exercise.search.domain.model.UserSearchResult

/**
 * MVP contract for User Search.
 */
interface UserSearchContract {

    /**
     * Callbacks to notify the view of the outcome of search queries initiated.
     */
    interface View {
        /**
         * Call when [UserSearchResult] are returned.
         */
        fun onUserSearchResults(results: List<UserSearchResult>)

        /**
         * Call when a loading screen is to be displayed
         */
        fun onUserSearchLoading()

        /**
         * Call when an error occurs during the execution of search queries.
         */
        fun onUserSearchError(error: String)
        fun setSearchTerm(word: String)
    }

    data class UiState<T>(val searchTerm: String, val domainState: DomainResult<T>)

    interface Presenter {
        /**
         * Call to attach a [Presenter] and provide its [View].
         */
        fun attach(view: View)

        /**
         * Call to detach a [Presenter] and clean up resources.
         */
        fun detach()

        /**
         * Notifies the presenter that the [searchTerm] has changed.
         */
        fun onQueryTextChange(searchTerm: String)
    }
}
