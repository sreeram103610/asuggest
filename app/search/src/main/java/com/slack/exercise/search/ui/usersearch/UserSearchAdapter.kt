package com.slack.exercise.search.ui.usersearch

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.slack.exercise.search.R
import com.slack.exercise.search.databinding.ItemUserSearchBinding
import com.slack.exercise.search.domain.model.UserSearchResult

/**
 * Adapter for the list of [UserSearchResult].
 */
class UserSearchAdapter(private val context: Context, private val imageLoader: ImageLoader) : RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {
  private var userSearchResults: List<UserSearchResult> = emptyList()

  private lateinit var roundedCorner: RoundedCornersTransformation

  fun setResults(results: List<UserSearchResult>) {
    DiffUtil.calculateDiff(UserSearchDiffCallback(userSearchResults, results)).apply {
      userSearchResults = results
      dispatchUpdatesTo(this@UserSearchAdapter)
    }
  }

  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    super.onAttachedToRecyclerView(recyclerView)
    roundedCorner = RoundedCornersTransformation(context.resources.getDimension(R.dimen.userimage_corner_radius))
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
    holder.name.text = userSearchResults[position].name
    holder.userImage.load(userSearchResults[position].imageUri, imageLoader = imageLoader) {
      placeholder(R.drawable.placeholder)
      transformations(roundedCorner)
    }
  }

  class UserSearchViewHolder(binding: ItemUserSearchBinding) : RecyclerView.ViewHolder(binding.root) {

    val username: TextView = binding.username
    val name: TextView = binding.name
    val userImage: ImageView = binding.userimage
  }
}