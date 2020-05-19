package com.maxk.marvy.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.R
import com.maxk.marvy.databinding.MarvelCharactersListBinding
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharactersListFragment : Fragment() {
    interface MarvelCharacterSelectionListener {
        fun onMarvelCharacterSelected(character: MarvelCharacter)
    }

    private lateinit var binding: MarvelCharactersListBinding

    var characterSelectionListener: MarvelCharacterSelectionListener? = null

    private val adapter: MarvelCharactersAdapter by lazy {
        MarvelCharactersAdapter(object :
            MarvelCharactersAdapter.CharacterClickListener {
            override fun onClick(character: MarvelCharacter, view: View) {
                characterSelectionListener?.onMarvelCharacterSelected(character)
            }
        })
    }

    var displaysPagingIndicator: Boolean = false
        set(value) {
            field = value
            adapter.displaysPagingIndicator = value
        }

    val listView: RecyclerView? get() = binding.charactersList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarvelCharactersListBinding.inflate(inflater, container, false)

        binding.charactersList.layoutManager = createRecyclerViewLayoutManager()
        binding.charactersList.adapter = adapter

        return binding.root
    }

    private fun createRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val spanCount = resources.getInteger(R.integer.marvelCharactersGridSpanCount)
        val layoutManager = GridLayoutManager(activity, spanCount)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (displaysPagingIndicator) {
                    return if (position == adapter.itemCount - 1) spanCount else 1
                }

                return 1
            }
        }

        return layoutManager
    }

    fun submitList(characters: PagedList<MarvelCharacter>?) = with(binding.charactersList) {
        this@MarvelCharactersListFragment.adapter.submitList(characters)
        post { scheduleLayoutAnimation() }
    }

    fun showEmptyView(show: Boolean) {
        binding.charactersList.isGone = show
        binding.emptyView.isGone = !show
    }
}
