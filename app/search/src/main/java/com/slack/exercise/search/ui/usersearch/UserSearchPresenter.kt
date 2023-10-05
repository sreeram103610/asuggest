package com.slack.exercise.search.ui.usersearch

import com.slack.exercise.search.domain.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.search.domain.usecase.DomainResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * Presenter responsible for reacting to user inputs and initiating search queries.
 */
class UserSearchPresenter @Inject constructor(
    private val userNameResultDataProvider: UserSearchResultDataProvider
) : UserSearchContract.Presenter {

  private var view: UserSearchContract.View? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val searchFlow: MutableSharedFlow<String> = MutableSharedFlow(extraBufferCapacity = 1)

  override fun attach(view: UserSearchContract.View) {
    this.view = view
    scope.launch {
          searchFlow.debounce(250.milliseconds).flatMapLatest { userNameResultDataProvider.fetchUsers(it) }
              .collect {
                  withContext(Dispatchers.Main) {
                      when (it) {
                          is DomainResult.Error -> this@UserSearchPresenter.view?.onUserSearchError(it.type.toString())

                          is DomainResult.Loaded -> this@UserSearchPresenter.view?.onUserSearchResults(
                              it.data
                          )

                          DomainResult.Loading -> this@UserSearchPresenter.view?.onUserSearchLoading()
                      }
                  }
              }
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