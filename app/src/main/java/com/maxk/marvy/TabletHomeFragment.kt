package com.maxk.marvy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.maxk.marvy.characters.TabletMarvelCharactersViewModel
import com.maxk.marvy.databinding.TabletHomeBinding
import com.maxk.marvy.view.alphabetlist.AlphabetListFragment
import com.maxk.marvy.view.alphabetlist.AlphabetListItem
import com.maxk.marvy.view.alphabetlist.AlphabetListItemSelectionListener

class TabletHomeFragment : Fragment(), AlphabetListItemSelectionListener {
    private lateinit var binding: TabletHomeBinding

    private var alphabetListFragment: AlphabetListFragment? = null

    private val marvelCharactersViewModel: TabletMarvelCharactersViewModel by activityViewModels()

    private val navController: NavController? by lazy<NavController?> {
        // Calling 'findNavController()' throws an IllegalStateException stating that there is no
        // nav controller attached. This workaround seems to work just fine.
        (childFragmentManager.findFragmentById(R.id.charactersNavHostFragment) as? NavHostFragment)
            ?.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = TabletHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alphabetListFragment = childFragmentManager.findFragmentById(R.id.alphabetListFragment)
                as? AlphabetListFragment
        alphabetListFragment?.selectionListener = this
    }

    override fun didSelectItem(item: AlphabetListItem) {
        navController?.popBackStack(R.id.marvelCharactersFragment, false)
        marvelCharactersViewModel.searchTerm.value = item.letter.toString()
    }
}
