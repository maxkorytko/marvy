package com.maxk.marvy.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.R
import com.maxk.marvy.api.Loading
import com.maxk.marvy.api.NetworkRequestStatusHandler
import com.maxk.marvy.characters.MarvelCharactersAdapter
import com.maxk.marvy.databinding.ActivitySearchBinding
import com.maxk.marvy.databinding.SearchViewBinding
import com.maxk.marvy.model.marvel.MarvelCharacter
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

    private val viewModel: SearchableViewModel by viewModels()

    private lateinit var searchRequestStatusHandler: NetworkRequestStatusHandler

    private val adapter: MarvelCharactersAdapter by lazy {
        MarvelCharactersAdapter(object :
            MarvelCharactersAdapter.CharacterClickListener {
            override fun onClick(character: MarvelCharacter, view: View) {

            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.charactersList.layoutManager = createRecyclerViewLayoutManager()
        binding.charactersList.adapter = adapter

        searchRequestStatusHandler =
            NetworkRequestStatusHandler(
                binding.charactersList,
                binding.errorView,
                binding.progressView
            )

        setupActionBar()
        setupObservers()
    }

    private fun createRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(this, 2)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val pagingRequestStatus = viewModel.pagingRequestStatus.value

                if (pagingRequestStatus is Loading && pagingRequestStatus.isInitialRequest == false) {
                    return if (position == adapter.itemCount - 1) 2 else 1
                }

                return 1
            }
        }

        return layoutManager
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("")?.apply {
            actionView = createSearchView()
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
            adapter.submitList(characters)
        }

        viewModel.searchRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            searchRequestStatusHandler.handle(requestStatus)
        }

        viewModel.pagingRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            adapter.displayPagingIndicator = requestStatus is Loading
        }
    }
}