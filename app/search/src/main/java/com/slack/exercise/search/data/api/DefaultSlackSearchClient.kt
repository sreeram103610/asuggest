package com.slack.exercise.search.data.api

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [DefaultSlackSearchClient] using [SlackSearchApi] to perform the API requests.
 */
internal class DefaultSlackSearchClient @Inject constructor(val service: SlackSearchApi) : SlackSearchClient {

  override fun searchUsers(searchTerm: String): Single<List<User>> {
    return service.searchUsers(searchTerm)
        .map {
          it.users
        }
        .subscribeOn(Schedulers.io())

  }
}
