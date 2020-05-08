package com.maxk.marvy.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.maxk.marvy.extensions.overrideExitTransition
import com.maxk.marvy.extensions.showKeyboard

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
        setupSearchView()
        setupObservers()
        setupEventListeners()
        allowToolbarToScroll(false)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.searchView.apply {
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

    private fun setupObservers() {
        viewModel.characters.observe({ lifecycle }) { characters ->
            charactersListFragment?.submitList(characters)
            showEmptyView(characters == null)
        }

        viewModel.searchRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            when (requestStatus) {
                is Loading -> showEmptyView(false)
                is Complete -> requestStatus.result.onSuccess { metadata ->
                    showEmptyView(metadata.itemsFetched == 0)
                    allowToolbarToScroll( metadata.itemsFetched > 0)
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
            binding.searchView.clearFocus()
            false
        }
    }

    private fun showEmptyView(show: Boolean) {
        charactersListFragment?.showEmptyView(show)
    }

    private fun allowToolbarToScroll(scroll: Boolean) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun finish() {
        super.finish()
        overrideExitTransition()
    }
}
