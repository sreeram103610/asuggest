package com.slack.exercise.search.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * User model returned by the API.
 */

@JsonClass(generateAdapter = true)
data class UserDto(
    val username: String,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    val id: Int
)
