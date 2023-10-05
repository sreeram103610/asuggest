package com.slack.exercise.search.data.api

import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.data.model.UserDto
import javax.inject.Inject

/**
 * Implementation of [DefaultSlackSearchClient] using [SlackSearchApi] to perform the API requests.
 */
internal class DefaultSlackSearchClient @Inject constructor(val service: SlackSearchApi) :
    SlackSearchClient {

    override suspend fun searchUsers(searchTerm: String): DataResult<List<UserDto>, ErrorResponse> {
        return service.searchUsers(searchTerm).let {
            if (it.isSuccessful) {
                DataResult.Success(it.body()?.userDtos ?: emptyList())
            } else {
                DataResult.Error(ErrorResponse.ServerError)
            }
        }
    }
}
