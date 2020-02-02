package com.maxk.marvy.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.maxk.marvy.R
import com.maxk.marvy.databinding.ActivityMainBinding
import com.maxk.marvy.viewmodels.MainActivityViewModel

class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        binding.alphabetViewPager.adapter = ViewPagerAdapter(this)
        setActionBar(binding.toolbar)
        setupTabBar()
    }

    private fun setupTabBar() {
        val mediator = TabLayoutMediator(binding.tabs, binding.alphabetViewPager) { tab, position ->
            tab.text = MainActivityViewModel.alphabet[position]
        }
        mediator.attach()
    }

    private inner class ViewPagerAdapter(activity: FragmentActivity):
        FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = MainActivityViewModel.alphabet.size

        override fun createFragment(position: Int): Fragment {
            return MarvelCharactersFragment()
        }
    }
}
