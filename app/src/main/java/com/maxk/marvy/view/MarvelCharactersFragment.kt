package com.maxk.marvy.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        binding.charactersList.layoutManager = GridLayoutManager(activity, 2)
        binding.charactersList.adapter = adapter
        setupObservations()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupObservations() {
        viewModel.characters.observe(this, Observer { result ->
            result.success { adapter.submitList(it.data.results) }

            result.error {
                Toast.makeText(
                    context?.applicationContext,
                    "Failed to fetch characters",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
