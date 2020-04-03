package com.maxk.marvy.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.R
import com.maxk.marvy.adapter.PagingIndicatorViewHolder
import com.maxk.marvy.databinding.MarvelCharacterListItemBinding
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharactersAdapter(private val characterClickListener: CharacterClickListener)
    : PagedListAdapter<MarvelCharacter, RecyclerView.ViewHolder>(DiffCallback()) {

    interface CharacterClickListener {
        fun onClick(character: MarvelCharacter, view: View)
    }

    class ViewHolder(private val binding: MarvelCharacterListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val binding = MarvelCharacterListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(
                    binding
                )
            }
        }

        fun bind(character: MarvelCharacter?, characterClickListener: CharacterClickListener) {
            if (character == null) return

            binding.character = character
            binding.imageView.image = character.thumbnail
            binding.characterClickListener = characterClickListener

            with(binding.imageView) {
                alpha = 0f
                scaleX = 1.05f
                scaleY = 1.05f
                animate()
                    .alpha(1f).scaleX(1f).scaleY(1f)
                    .start()
            }
        }
    }

    var displaysPagingIndicator: Boolean = false
        set(value) {
            field = value
            if (value) {
                notifyItemInserted(super.getItemCount())
            } else {
                notifyItemRemoved(super.getItemCount())
            }
        }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (displaysPagingIndicator) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (displaysPagingIndicator && position == itemCount - 1) {
            R.layout.paging_indicator_layout
        } else {
            R.layout.marvel_character_list_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.paging_indicator_layout -> PagingIndicatorViewHolder.create(
                parent
            )
            R.layout.marvel_character_list_item -> ViewHolder.create(
                parent
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.marvel_character_list_item -> bindCharacterViewHolder(holder, position)
        }
    }

    private fun bindCharacterViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(getItem(position), characterClickListener)
    }
}

private class DiffCallback: DiffUtil.ItemCallback<MarvelCharacter>() {
    override fun areItemsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter): Boolean {
        return oldItem.name == newItem.name
    }
}