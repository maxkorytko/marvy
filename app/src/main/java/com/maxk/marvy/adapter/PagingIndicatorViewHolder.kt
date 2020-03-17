package com.maxk.marvy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.databinding.PagingIndicatorLayoutBinding

class PagingIndicatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): PagingIndicatorViewHolder {
            val binding = PagingIndicatorLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PagingIndicatorViewHolder(binding.root)
        }
    }
}