package com.maxk.marvy.view.alphabetlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import com.maxk.marvy.databinding.AlphabetListBinding

typealias AlphabetListSelectionKeyType = Long

class AlphabetListFragment : Fragment() {
    private lateinit var binding: AlphabetListBinding

    private lateinit var selectionTracker: SelectionTracker<AlphabetListSelectionKeyType>

    private val adapter: AlphabetListAdapter by lazy { AlphabetListAdapter() }

    var selectionListener: AlphabetListItemSelectionListener?
        get() { return adapter.selectionListener }
        set(value) { adapter.selectionListener = value }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlphabetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            alphabetList.adapter = adapter
            setupAlphabetListSelection(alphabetList)
            adapter.selectionTracker = selectionTracker
        }
    }

    private fun setupAlphabetListSelection(alphabetList: RecyclerView) {
        selectionTracker = SelectionTracker.Builder(
            "alphabet-list-selection",
            alphabetList,
            StableIdKeyProvider(alphabetList),
            AlphabetItemDetailsLookup(
                alphabetList
            ),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
            .withOnItemActivatedListener { itemDetails, _ -> updateSelection(itemDetails)}
            .build()
    }

    private fun updateSelection(
        itemDetails: ItemDetailsLookup.ItemDetails<AlphabetListSelectionKeyType>): Boolean {

        itemDetails.selectionKey?.let { selectionTracker.select(it) }
        return true
    }
}

//region AlphabetItemDetailsLookup
private class AlphabetItemDetailsLookup(private val recyclerView: RecyclerView)
    : ItemDetailsLookup<AlphabetListSelectionKeyType>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<AlphabetListSelectionKeyType>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null

        val viewHolder = recyclerView.getChildViewHolder(view) as? AlphabetListAdapter.ViewHolder
        return viewHolder?.itemDetails
    }
}
//endregion
