package com.slack.exercise.search.ui.usersearch

import com.slack.exercise.search.domain.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.search.domain.model.DomainResult
import com.slack.exercise.search.domain.model.UserSearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

/**
 * Presenter responsible for reacting to user inputs and initiating search queries.
 */
@Singleton
class UserSearchPresenter @Inject constructor(
    private val userSearchResultDataProvider: UserSearchResultDataProvider
) : UserSearchContract.Presenter {

    private var view: UserSearchContract.View? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val searchFlow: MutableSharedFlow<String> = MutableSharedFlow(extraBufferCapacity = 1)
    private var lastState: UserSearchContract.UiState<List<UserSearchResult>>? = null

    override fun attach(view: UserSearchContract.View) {
        this.view = view
        // Handles config changes
        lastState?.let {
            view.setSearchTerm(it.searchTerm)
            applyUi(it.domainState)
        }
        scope.launch {
            searchFlow.debounce(250.milliseconds)
                .flatMapLatest { searchTerm ->
                    userSearchResultDataProvider.fetchUsers(searchTerm).map { Pair(searchTerm, it) }
                }
                .collect {
                    lastState = UserSearchContract.UiState(it.first, it.second)
                    withContext(Dispatchers.Main) {
                        applyUi(lastState!!.domainState)
                    }
                }
        }
    }

    private fun applyUi(result: DomainResult<List<UserSearchResult>>) {
        when (result) {
            is DomainResult.Error -> this@UserSearchPresenter.view?.onUserSearchError(result.type.toString())

            is DomainResult.Loaded -> this@UserSearchPresenter.view?.onUserSearchResults(
                result.data
            )

            is DomainResult.Loading -> this@UserSearchPresenter.view?.onUserSearchLoading()
        }
    }

    override fun detach() {
        view = null
        scope.cancel()
    }

    override fun onQueryTextChange(searchTerm: String) {
        searchFlow.tryEmit(searchTerm)
    }
}
