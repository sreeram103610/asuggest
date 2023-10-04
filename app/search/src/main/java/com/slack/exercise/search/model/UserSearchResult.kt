package com.slack.exercise.search.model

import com.slack.exercise.search.data.model.UserDto

/**
 * Models users returned by the API.
 */
data class UserSearchResult(val username: String, val imageUri: String, val name: String, val id: Int) {
    companion object {
        fun from(dto: UserDto) = UserSearchResult(username = dto.username, imageUri = dto.avatarUrl, name = dto.displayName, id = dto.id)
    }
}

