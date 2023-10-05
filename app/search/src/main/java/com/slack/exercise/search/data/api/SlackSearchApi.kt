package com.slack.exercise.search.data.api

import com.slack.exercise.search.data.model.UserDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for querying the Slack Search API.
 */
interface SlackSearchApi {

    /**
     * Retrieves users matching the specified query.
     *
     * @param query The search query.
     * @return The search response.
     */
    @GET("search")
    suspend fun searchUsers(@Query("query") query: String): Response<UserSearchResponse>
}

/**
 * Represents the response of a user search query.
 *
 * @property ok Indicates if the search was successful.
 * @property userDtos List of matched users.
 */
@JsonClass(generateAdapter = true)
data class UserSearchResponse(
    val ok: Boolean,
    @Json(name = "users") val userDtos: List<UserDto>
)
