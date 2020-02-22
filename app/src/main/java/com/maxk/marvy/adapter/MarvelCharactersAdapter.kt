package com.maxk.marvy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.databinding.MarvelCharacterListItemBinding
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharactersAdapter(private val characterClickListener: CharacterClickListener)
    : ListAdapter<MarvelCharacter, MarvelCharactersAdapter.ViewHolder>(DiffCallback()) {

    interface CharacterClickListener {
        fun onClick(character: MarvelCharacter)
    }

    class ViewHolder(private val binding:
                     MarvelCharacterListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(character: MarvelCharacter, characterClickListener: CharacterClickListener) {
            binding.character = character
            binding.imageView.image = character.thumbnail
            binding.characterClickListener = characterClickListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MarvelCharacterListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), characterClickListener)
    }
}

private class DiffCallback: DiffUtil.ItemCallback<MarvelCharacter>() {
    override fun areItemsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter): Boolean {
        return oldItem.name == newItem.name
    }
}