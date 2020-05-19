package com.maxk.marvy.characters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.maxk.marvy.MainActivity
import com.maxk.marvy.databinding.MarvelCharactersViewPagerBinding
import com.maxk.marvy.extensions.overrideEnterTransition
import com.maxk.marvy.model.ALPHABET
import com.maxk.marvy.search.SearchableActivity

class MarvelCharactersViewPagerFragment: Fragment() {
    private lateinit var binding: MarvelCharactersViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MarvelCharactersViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleView.onSearchBarClicked = {
            SearchableActivity.start(activity)
            activity?.overrideEnterTransition()
        }

        binding.alphabetViewPager.adapter = ViewPagerAdapter(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.let { activity ->
            activity.setActionBar(binding.toolbar)
            setupTabBar()
        }
    }

    private fun setupTabBar() {
        val mediator = TabLayoutMediator(binding.tabs, binding.alphabetViewPager) { tab, position ->
            tab.text = ALPHABET[position].toString()
        }
        mediator.attach()
    }

    private inner class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = ALPHABET.size

        override fun createFragment(position: Int): Fragment {
            return MarvelCharactersFragment.newInstance(searchTerm = ALPHABET[position].toString())
        }
    }
}
