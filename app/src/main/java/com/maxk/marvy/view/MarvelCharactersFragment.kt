package com.maxk.marvy.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maxk.marvy.adapter.MarvelCharactersAdapter
import com.maxk.marvy.databinding.MarvelCharactersBinding
import com.maxk.marvy.viewmodels.MarvelCharactersViewModel


class MarvelCharactersFragment : Fragment() {
    private val viewModel: MarvelCharactersViewModel by viewModels()

    private val adapter: MarvelCharactersAdapter by lazy { MarvelCharactersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = MarvelCharactersBinding.inflate(layoutInflater, container, false)

        binding.charactersList.adapter = adapter
        setupObservations()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupObservations() {
        viewModel.characters.observe(this, Observer { characters ->
            Log.d(MarvelCharactersFragment::class.java.simpleName, "$characters.size")
            adapter.submitList(characters.data.results)
        })
    }
}
