package com.slack.exercise.search.data.api

import com.slack.exercise.search.data.model.UserDto
import com.squareup.moshi.JsonClass
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SlackSearchApi {
  /**
   * Search query. Returns a [Single] emitting the API response.
   */
  @GET("search")
  suspend fun searchUsers(@Query("query") query: String): Response<UserSearchResponse>
}

/**
 * Models the search query response.
 */
@JsonClass(generateAdapter = true)
data class UserSearchResponse(val ok: Boolean, val userDtos: List<UserDto>)