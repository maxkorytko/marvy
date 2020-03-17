package com.maxk.marvy.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.adapter.MarvelCharactersAdapter
import com.maxk.marvy.databinding.MarvelCharactersBinding
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.result.Loading
import com.maxk.marvy.result.NetworkRequestStatusHandler
import com.maxk.marvy.viewmodels.MarvelCharactersViewModel


class MarvelCharactersFragment(private val searchTerm: String) : Fragment() {


    private val viewModel: MarvelCharactersViewModel by viewModels(
        factoryProducer = { MarvelCharactersViewModel.Factory(searchTerm) }
    )

    private lateinit var binding: MarvelCharactersBinding

    private val adapter: MarvelCharactersAdapter by lazy {
        MarvelCharactersAdapter(object : MarvelCharactersAdapter.CharacterClickListener {
            override fun onClick(character: MarvelCharacter) {
                show(character)
            }
        })
    }

    private lateinit var pagingRequestStatusHandler: NetworkRequestStatusHandler

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MarvelCharactersBinding.inflate(layoutInflater, container, false)

        binding.charactersList.layoutManager = createRecyclerViewLayoutManager()
        binding.charactersList.adapter = adapter

        pagingRequestStatusHandler = NetworkRequestStatusHandler(
            binding.charactersList,
            binding.errorView,
            binding.progressView
        )

        setupObservations()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun createRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val layoutManager = GridLayoutManager(activity, 2)

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

    private fun setupObservations() {
        viewModel.pagingRequestStatus.observe({ this.lifecycle }) { requestStatus ->
            if (requestStatus.isInitialRequest == true) {
                pagingRequestStatusHandler.handle(requestStatus)
            } else {
                adapter.displayPagingIndicator = requestStatus is Loading
            }
        }

        viewModel.characters.observe({ this.lifecycle}) {
            adapter.submitList(it)
        }
    }

    private fun show(character: MarvelCharacter) {
        MarvelCharacterActivity.start(this.context, character)
    }
}
