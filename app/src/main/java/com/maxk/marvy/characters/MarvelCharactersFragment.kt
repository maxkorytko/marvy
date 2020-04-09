package com.maxk.marvy.characters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maxk.marvy.R
import com.maxk.marvy.api.Loading
import com.maxk.marvy.api.NetworkRequestStatusHandler
import com.maxk.marvy.characters.viewmodels.MarvelCharactersViewModel
import com.maxk.marvy.databinding.MarvelCharactersBinding

class MarvelCharactersFragment(private val searchTerm: String) : Fragment() {
    private val viewModel: MarvelCharactersViewModel by viewModels(
        factoryProducer = { MarvelCharactersViewModel.Factory(searchTerm) }
    )

    private lateinit var binding: MarvelCharactersBinding

    private lateinit var pagingRequestStatusHandler: NetworkRequestStatusHandler

    private var charactersListFragment: MarvelCharactersListFragment? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MarvelCharactersBinding.inflate(layoutInflater, container, false)

        charactersListFragment = childFragmentManager.findFragmentById(
            R.id.characters_list_fragment) as? MarvelCharactersListFragment

        pagingRequestStatusHandler = NetworkRequestStatusHandler(
            charactersListFragment?.view,
            binding.errorView,
            binding.progressView
        )

        setupObservations()

        return binding.root
    }

    private fun setupObservations() {
        viewModel.pagingRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            if (requestStatus.isInitialRequest == true) {
                pagingRequestStatusHandler.handle(requestStatus)
            } else {
                charactersListFragment?.displaysPagingIndicator = requestStatus is Loading
            }
        }

        viewModel.characters.observe({ this.lifecycle}) { characters ->
            charactersListFragment?.submitList(characters)
        }
    }
}
