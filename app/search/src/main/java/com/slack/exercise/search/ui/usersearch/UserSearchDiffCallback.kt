package com.slack.exercise.search.ui.usersearch

import androidx.recyclerview.widget.DiffUtil
import com.slack.exercise.search.domain.model.UserSearchResult

class UserSearchDiffCallback(
    private val oldList: List<UserSearchResult>,
    private val newList: List<UserSearchResult>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
}
