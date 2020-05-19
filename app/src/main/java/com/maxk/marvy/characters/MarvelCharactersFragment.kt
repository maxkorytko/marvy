package com.maxk.marvy.characters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxk.marvy.R
import com.maxk.marvy.api.Loading
import com.maxk.marvy.api.NetworkRequestStatusHandler
import com.maxk.marvy.characters.viewmodels.MarvelCharactersViewModel
import com.maxk.marvy.databinding.MarvelCharactersBinding
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharactersFragment : Fragment(),
    MarvelCharactersListFragment.MarvelCharacterSelectionListener {
    companion object {
        private const val SEARCH_TERM = "marvel_characters_search_term"
        private const val IS_TABLET = "marvel_characters_is_tablet"

        fun newInstance(searchTerm: String?, isTablet: Boolean = false): MarvelCharactersFragment =
            MarvelCharactersFragment().apply {
                arguments = Bundle().apply {
                    putString(SEARCH_TERM, searchTerm)
                    putBoolean(IS_TABLET, isTablet)
                }
            }
    }

    private val searchTerm: String? by lazy { arguments?.getString(SEARCH_TERM) }

    private val isTablet: Boolean by lazy { arguments?.getBoolean(IS_TABLET) ?: false }

    private val viewModel: MarvelCharactersViewModel by viewModels(
        factoryProducer = { MarvelCharactersViewModel.Factory(searchTerm ?: "") }
    )

    private lateinit var binding: MarvelCharactersBinding

    private lateinit var pagingRequestStatusHandler: NetworkRequestStatusHandler

    private var charactersListFragment: MarvelCharactersListFragment? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MarvelCharactersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charactersListFragment = childFragmentManager.findFragmentById(
            R.id.characters_list_fragment) as? MarvelCharactersListFragment
        charactersListFragment?.characterSelectionListener = this

        pagingRequestStatusHandler = NetworkRequestStatusHandler(
            charactersListFragment?.view,
            binding.errorView,
            binding.progressView
        )

        setupObservations()
    }

    private fun setupObservations() {
        viewModel.pagingRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            if (requestStatus.isInitialRequest == true) {
                pagingRequestStatusHandler.handle(requestStatus)
            } else {
                charactersListFragment?.displaysPagingIndicator = requestStatus is Loading
            }
        }

        viewModel.characters.observe({ this.lifecycle }) { characters ->
            charactersListFragment?.submitList(characters)
        }
    }

    override fun onMarvelCharacterSelected(character: MarvelCharacter) {
        if (isTablet) {
            showMarvelCharacter(character)
        } else {
            MarvelCharacterActivity.start(activity, character)
        }
    }

    private fun showMarvelCharacter(character: MarvelCharacter) {
        findNavController().navigate(
            TabletMarvelCharactersFragmentDirections.actionMarvelCharacter(character)
        )
    }
}
