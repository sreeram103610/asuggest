package com.slack.exercise.search.dataprovider

import com.slack.exercise.search.data.api.SlackSearchClient
import com.slack.exercise.search.model.UserSearchResult
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Implementation of [UserSearchResultDataProvider].
 */
class UserSearchResultDataProviderImpl @Inject constructor(
    private val slackApi: SlackSearchClient
) : UserSearchResultDataProvider {

  /**
   * Returns a [Single] emitting a set of [UserSearchResult].
   */
  override fun fetchUsers(searchTerm: String): Single<Set<UserSearchResult>> {
    return slackApi.searchUsers(searchTerm)
        .map {
          it.map { user -> UserSearchResult(user.username) }.toSet()
        }
  }
}