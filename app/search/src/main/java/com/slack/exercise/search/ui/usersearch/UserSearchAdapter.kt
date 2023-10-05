package com.slack.exercise.search.ui.usersearch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slack.exercise.search.databinding.ItemUserSearchBinding
import com.slack.exercise.search.domain.model.UserSearchResult

/**
 * Adapter for the list of [UserSearchResult].
 */
class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {
  private var userSearchResults: List<UserSearchResult> = emptyList()

  fun setResults(results: List<UserSearchResult>) {
    userSearchResults = results
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
    val view = ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return UserSearchViewHolder(view)
  }

  override fun getItemCount(): Int {
    return userSearchResults.size
  }

  override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
    holder.username.text = userSearchResults[position].username
  }

  class UserSearchViewHolder(binding: ItemUserSearchBinding) : RecyclerView.ViewHolder(binding.root) {

    val username: TextView = binding.username
  }
}