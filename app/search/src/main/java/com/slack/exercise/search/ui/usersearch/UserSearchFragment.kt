package com.slack.exercise.search.ui.usersearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import com.slack.exercise.search.R
import com.slack.exercise.search.databinding.FragmentUserSearchBinding
import com.slack.exercise.search.domain.model.UserSearchResult
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Main fragment displaying and handling interactions with the view.
 * We use the MVP pattern and attach a Presenter that will be in charge of non view related operations.
 */
class UserSearchFragment : DaggerFragment(), UserSearchContract.View {

  private var searchTerm: String? = null
  private lateinit var searchView: SearchView

  @Inject
  internal lateinit var presenter: UserSearchPresenter

  @Inject
  internal lateinit var imageLoader: ImageLoader

  private lateinit var userSearchBinding: FragmentUserSearchBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    userSearchBinding = FragmentUserSearchBinding.inflate(inflater, container, false)
    setHasOptionsMenu(true)
    return userSearchBinding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpToolbar()
    setUpList()
  }

  override fun onStart() {
    super.onStart()

    presenter.attach(this)
  }

  override fun onStop() {
    super.onStop()

    presenter.detach()
  }

  @Deprecated("Deprecated in Java")
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_user_search, menu)

    searchView = menu.findItem(R.id.search_menu_item).actionView as SearchView
    searchView.queryHint = getString(R.string.search_users_hint)
    searchTerm?.let { searchView.setQuery(it, false)
      searchView.isIconified = false
    }

    val editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
    editText.maxLines = 1

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        return true
      }

      override fun onQueryTextChange(newText: String): Boolean {
        presenter.onQueryTextChange(newText)
        return true
      }
    })
  }



  override fun onUserSearchResults(results: List<UserSearchResult>) {
    userSearchBinding.progressBar.hide()
    userSearchBinding.userSearchResultList.visibility = View.VISIBLE

    val adapter = userSearchBinding.userSearchResultList.adapter as UserSearchAdapter
    adapter.setResults(results.sortedBy { it.name })
    userSearchBinding.userSearchResultList.scrollToPosition(0)
  }

  override fun onUserSearchLoading() {
    userSearchBinding.progressBar.show()
    userSearchBinding.userSearchResultList.visibility = View.GONE
  }

  override fun onUserSearchError(error: String) {
    userSearchBinding.progressBar.hide()
    userSearchBinding.userSearchResultList.visibility = View.GONE
    Timber.e("Error searching users.")
  }

  override fun setSearchTerm(word: String) {
    searchTerm = word
  }

  private fun setUpToolbar() {
    val act = activity as? AppCompatActivity
    act?.setSupportActionBar(userSearchBinding.toolbar)
  }

  private fun setUpList() {

    val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
    ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let {
      dividerItemDecoration.setDrawable(it)
    }

    with(userSearchBinding.userSearchResultList) {
      adapter = UserSearchAdapter(context.applicationContext, imageLoader)
      layoutManager = LinearLayoutManager(activity).apply {
        orientation = LinearLayoutManager.VERTICAL
      }
      setHasFixedSize(true)
      addItemDecoration(dividerItemDecoration)
    }
  }
}