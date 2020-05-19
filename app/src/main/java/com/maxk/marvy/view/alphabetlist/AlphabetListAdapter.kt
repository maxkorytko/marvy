package com.maxk.marvy.view.alphabetlist

import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.databinding.AlphabetListItemBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.model.ALPHABET
import kotlin.properties.Delegates

data class AlphabetListItem(val letter: Char) {
    val selectionKey: AlphabetListSelectionKeyType get() = letter.toLong()
}

interface AlphabetListItemSelectionListener {
    fun didSelectItem(item: AlphabetListItem)
}

class AlphabetListAdapter : RecyclerView.Adapter<AlphabetListAdapter.ViewHolder>() {
    //region ViewHolder
    class ViewHolder(private val binding: AlphabetListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    AlphabetListItemBinding.inflate(parent.layoutInflater, parent, false)
                )
            }
        }

        private var item: AlphabetListItem? = null

        var isSelected: Boolean by Delegates.observable(false) { _, _, isActivated ->
            binding.root.isActivated = isActivated
        }

        val itemDetails: ItemDetailsLookup.ItemDetails<AlphabetListSelectionKeyType>?
            get() = object : ItemDetailsLookup.ItemDetails<AlphabetListSelectionKeyType>() {
                override fun getSelectionKey(): AlphabetListSelectionKeyType? = item?.selectionKey

                override fun getPosition(): Int = adapterPosition
            }

        fun bind(item: AlphabetListItem) {
            this.item = item
            binding.textView.text = item.letter.toString()
        }
    }
    //endregion

    private val items: List<AlphabetListItem> = ALPHABET.map {
        AlphabetListItem(letter = it)
    }

    var selectionTracker: SelectionTracker<AlphabetListSelectionKeyType>?
            by Delegates.observable<SelectionTracker<AlphabetListSelectionKeyType>?>(null)
            { _, _, _ -> selectItem(items.first()) }

    var selectedItem: AlphabetListItem? = null
        private set

    var selectionListener: AlphabetListItemSelectionListener? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        updateItemSelection(holder, items[position])
    }

    private fun updateItemSelection(holder: ViewHolder, item: AlphabetListItem) {
        val isSelected = selectionTracker?.isSelected(item.selectionKey) == true

        holder.isSelected = isSelected

        if (item == selectedItem && isSelected) {
            // Already selected. No need to select again.
            return
        }

        if (item == selectedItem && !isSelected && selectionTracker?.selection?.isEmpty == true) {
            // The only selected item just got deselected. There must be at least one selected
            // item at all times, so let's re-select this item.
            holder.itemView.post { selectItem(item) }
            return
        }

        if (isSelected) {
            selectedItem = item
            selectionListener?.didSelectItem(item)
        }
    }

    private fun selectItem(item: AlphabetListItem) {
        selectionTracker?.select(item.selectionKey)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].selectionKey
    }
}
