package com.maxk.marvy

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.maxk.marvy.databinding.ActivityMainBinding
import com.maxk.marvy.characters.MarvelCharactersFragment
import com.maxk.marvy.search.SearchableActivity

class MainActivity : FragmentActivity() {
    companion object {
        private val ALPHABET = listOf(
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z"
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        binding.titleView.onSearchBarClicked = {
            SearchableActivity.start(this)
        }

        binding.alphabetViewPager.adapter = ViewPagerAdapter(this)
        setActionBar(binding.toolbar)
        setupTabBar()
    }

    private fun setupTabBar() {
        val mediator = TabLayoutMediator(binding.tabs, binding.alphabetViewPager) { tab, position ->
            tab.text = ALPHABET[position]
        }
        mediator.attach()
    }

    private inner class ViewPagerAdapter(activity: FragmentActivity):
        FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = ALPHABET.size

        override fun createFragment(position: Int): Fragment {
            return MarvelCharactersFragment(searchTerm = ALPHABET[position])
        }
    }
}
