package com.maxk.marvy.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.AppBarLayout
import com.maxk.marvy.R
import com.maxk.marvy.api.Complete
import com.maxk.marvy.api.Loading
import com.maxk.marvy.api.NetworkRequestStatusHandler
import com.maxk.marvy.characters.MarvelCharactersListFragment
import com.maxk.marvy.databinding.ActivitySearchBinding
import com.maxk.marvy.databinding.SearchViewBinding
import com.maxk.marvy.view.showKeyboard

class SearchableActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context?) {
            context?.let {
                val intent = Intent(it, SearchableActivity::class.java)
                ActivityCompat.startActivity(it, intent, null)
            }
        }
    }

    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchRequestStatusHandler: NetworkRequestStatusHandler

    private var charactersListFragment: MarvelCharactersListFragment? = null

    private var searchView: SearchView? = null

    private val viewModel: SearchableViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        charactersListFragment = supportFragmentManager.findFragmentById(
            R.id.characters_list_fragment) as? MarvelCharactersListFragment

        searchRequestStatusHandler =
            NetworkRequestStatusHandler(
                charactersListFragment?.view,
                binding.errorView,
                binding.progressView
            )

        setupActionBar()
        setupObservers()
        setupEventListeners()
        updateCollapsingToolbarScrollFlags(scroll = false)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("")?.apply {
            searchView = createSearchView()
            actionView = searchView
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        return true
    }

    private fun createSearchView(): SearchView? {
        return SearchViewBinding.inflate(LayoutInflater.from(this)).run {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

            return (root as? SearchView)?.apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                showKeyboard()
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.search(newText)
                        return true
                    }
                })
            }
        }
    }

    private fun setupObservers() {
        viewModel.characters.observe({ lifecycle }) { characters ->
            charactersListFragment?.submitList(characters)
        }

        viewModel.searchRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            when (requestStatus) {
                is Loading -> showEmptyView(false)
                is Complete -> requestStatus.result.onSuccess { metadata ->
                    showEmptyView(metadata.itemsFetched == 0)
                    updateCollapsingToolbarScrollFlags(scroll = metadata.itemsFetched > 0)
                }
            }

            searchRequestStatusHandler.handle(requestStatus)
        }

        viewModel.pagingRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            charactersListFragment?.displaysPagingIndicator = requestStatus is Loading
        }
    }

    private fun setupEventListeners() {
        charactersListFragment?.listView?.setOnTouchListener { _, _ -> Boolean
            searchView?.clearFocus()
            false
        }
    }

    private fun showEmptyView(show: Boolean) {
        charactersListFragment?.showEmptyView(show)
    }

    private fun updateCollapsingToolbarScrollFlags(scroll: Boolean) {
        val layoutParams = binding.collapsingToolbar.layoutParams as? AppBarLayout.LayoutParams
        layoutParams?.let {
            it.scrollFlags = if (scroll) {
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            } else {
                AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            }

            binding.collapsingToolbar.layoutParams = it
        }
    }
}
