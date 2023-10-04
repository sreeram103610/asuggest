package com.slack.exercise.search.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * User model returned by the API.
 */

@JsonClass(generateAdapter = true)
data class User(val username: String,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "avatar_url")
    val avatarUrl: String)