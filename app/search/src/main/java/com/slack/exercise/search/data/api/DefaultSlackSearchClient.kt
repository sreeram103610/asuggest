package com.slack.exercise.search.data.api

import com.slack.exercise.search.data.model.DataResult
import com.slack.exercise.search.data.model.ErrorResponse
import com.slack.exercise.search.data.model.UserDto
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

/**
 * Provides a default implementation of [SlackSearchClient], utilizing [SlackSearchApi] to execute user search requests.
 *
 * @property service The [SlackSearchApi] service used to perform API requests.
 */
internal class DefaultSlackSearchClient @Inject constructor(val service: SlackSearchApi) :
    SlackSearchClient {

    /**
     * Searches for users based on the provided search term. Utilizes the [SlackSearchApi] to perform
     * the search and returns a [DataResult] containing either the list of found users or an error response.
     *
     * @param searchTerm The term used for searching users.
     * @return A [DataResult] containing either a list of [UserDto] if the search is successful,
     * or an [ErrorResponse] if there's an error during the search.
     */
    override suspend fun searchUsers(searchTerm: String): DataResult<List<UserDto>, ErrorResponse> {
        return try {
            service.searchUsers(searchTerm).let {
                if (it.isSuccessful) {
                    DataResult.Success(it.body()?.userDtos ?: emptyList())
                } else {
                    DataResult.Error(ErrorResponse.ServerError)
                }
            }
        } catch (e: Exception) {
            Timber.e("Network Error. ${e.message}")
            DataResult.Error(ErrorResponse.ServerError)
        }
    }
}