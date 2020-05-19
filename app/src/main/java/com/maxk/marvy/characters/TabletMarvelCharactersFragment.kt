package com.maxk.marvy.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.maxk.marvy.R
import com.maxk.marvy.databinding.TabletMarvelCharactersBinding
import com.maxk.marvy.search.SearchableActivity

class TabletMarvelCharactersViewModel : ViewModel() {
    val searchTerm: MutableLiveData<String> = MutableLiveData()
}

class TabletMarvelCharactersFragment : Fragment() {
    private lateinit var binding: TabletMarvelCharactersBinding

    private val viewModel: TabletMarvelCharactersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TabletMarvelCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        viewModel.searchTerm.observe(viewLifecycleOwner, Observer { searchTerm ->
            showCharacters(searchTerm)
        })
    }

    private fun showCharacters(searchTerm: String?) {
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.marvelCharactersFragmentContainer,
                MarvelCharactersFragment.newInstance(searchTerm, true)
            )
            .commit()
    }

    private fun setupToolbar() = with(findNavController()) {
        val appBarConfiguration = AppBarConfiguration(graph)
        binding.toolbar.setupWithNavController(this, appBarConfiguration)
        binding.titleView.onSearchBarClicked = {
            SearchableActivity.start(context)
        }
    }
}