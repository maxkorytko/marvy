package com.maxk.marvy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.databinding.MarvelCharacterListItemBinding
import com.maxk.marvy.model.marvel.Character

class MarvelCharactersAdapter:
    ListAdapter<Character, MarvelCharactersAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding:
                     MarvelCharacterListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character) {
            binding.characterName.text = item.name
            binding.imageView.image = item.thumbnail
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
        holder.bind(getItem(position))
    }
}

private class DiffCallback: DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.name == newItem.name
    }
}