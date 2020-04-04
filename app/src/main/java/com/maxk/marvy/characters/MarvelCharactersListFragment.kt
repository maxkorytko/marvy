package com.maxk.marvy.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.R
import com.maxk.marvy.databinding.MarvelCharactersListBinding
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharactersListFragment : Fragment() {
    private lateinit var binding: MarvelCharactersListBinding

    private val adapter: MarvelCharactersAdapter by lazy {
        MarvelCharactersAdapter(object :
            MarvelCharactersAdapter.CharacterClickListener {
            override fun onClick(character: MarvelCharacter, view: View) {
                show(character, sharedElement = view.findViewById(R.id.characterName))
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
        val layoutManager = GridLayoutManager(activity, 2)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (displaysPagingIndicator) {
                    return if (position == adapter.itemCount - 1) 2 else 1
                }

                return 1
            }
        }

        return layoutManager
    }

    private fun show(character: MarvelCharacter, sharedElement: View) {
        if (activity == null) {
            MarvelCharacterActivity.start(context, character)
            return
        }

        activity?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                it,
                sharedElement,
                MarvelCharacterActivity.VIEW_NAME_CHARACTER_NAME
            )
            MarvelCharacterActivity.start(this.context, character, options.toBundle())
        }
    }

    fun submitList(characters: PagedList<MarvelCharacter>) {
        adapter.submitList(characters)
    }
}
