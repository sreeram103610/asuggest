package com.slack.exercise.search.ui.usersearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
 * Main fragment for displaying and handling interactions with the user search view.
 * We use the MVP pattern and attach a Presenter that will be in charge of non view related operations.
 */
class UserSearchFragment : DaggerFragment(), UserSearchContract.View {

    @Inject
    lateinit var presenter: UserSearchPresenter

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var userSearchAdapter: UserSearchAdapter

    private lateinit var binding: FragmentUserSearchBinding
    private lateinit var searchView: SearchView
    private var searchTerm: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
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
        presenter.detach()
        super.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_search, menu)
        setupSearchView(menu)
    }

    override fun onUserSearchResults(results: List<UserSearchResult>) {
        with(binding) {
            progressBar.visibility = View.GONE
            userSearchResultList.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            userSearchResultList.scrollToPosition(0)
        }
        userSearchAdapter.setResults(results.sortedBy { it.name })
    }

    override fun onUserSearchLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            userSearchResultList.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }
    }

    override fun onUserSearchError(error: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            userSearchResultList.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
        }
        Timber.e("Error searching users.")
    }

    override fun setSearchTerm(word: String) {
        searchTerm = word
    }

    private fun setUpToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    private fun setUpList() {
        with(binding.userSearchResultList) {
            adapter = userSearchAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                ContextCompat.getDrawable(requireContext(), R.drawable.divider)?.let { setDrawable(it) }
            })
        }
    }

    private fun setupSearchView(menu: Menu) {
        searchView = menu.findItem(R.id.search_menu_item).actionView as SearchView
        searchView.apply {
            queryHint = getString(R.string.search_users_hint)
            imeOptions = imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            findViewById<EditText>(androidx.appcompat.R.id.search_src_text).maxLines = 1

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) = true

                override fun onQueryTextChange(newText: String): Boolean {
                    presenter.onQueryTextChange(newText)
                    return true
                }
            })

            searchTerm?.let {
                setQuery(it, false)
                isIconified = false
            }
        }
    }
}
