package com.maxk.marvy.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.maxk.marvy.adapter.MarvelCharactersAdapter
import com.maxk.marvy.databinding.MarvelCharactersBinding
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.result.NetworkResourceHandler
import com.maxk.marvy.result.ifComplete
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

    private lateinit var charactersHandler: NetworkResourceHandler

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MarvelCharactersBinding.inflate(layoutInflater, container, false)

        binding.charactersList.layoutManager = GridLayoutManager(activity, 2)
        binding.charactersList.adapter = adapter

        charactersHandler = NetworkResourceHandler(
            binding.charactersList,
            binding.errorView,
            binding.progressView
        )

        setupObservations()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupObservations() {
        viewModel.characters.observe({ this.lifecycle }) { characters ->
            charactersHandler.handle(characters)

            characters.ifComplete { result ->
                result.success { adapter.submitList(it.data.results) }
            }
        }
    }

    private fun show(character: MarvelCharacter) {
        MarvelCharacterActivity.start(this.context, character)
    }
}
