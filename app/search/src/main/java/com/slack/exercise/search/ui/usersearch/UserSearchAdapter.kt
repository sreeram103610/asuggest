package com.slack.exercise.search.ui.usersearch

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.slack.exercise.search.databinding.ItemUserSearchBinding
import com.slack.exercise.search.model.UserSearchResult

/**
 * Adapter for the list of [UserSearchResult].
 */
class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {
  private var userSearchResults: List<UserSearchResult> = emptyList()

  fun setResults(results: Set<UserSearchResult>) {
    userSearchResults = results.toList()
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