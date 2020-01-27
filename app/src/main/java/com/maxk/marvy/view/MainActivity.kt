package com.maxk.marvy.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.maxk.marvy.R
import com.maxk.marvy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        setSupportActionBar(binding.toolbar)
        setupNavigationController()
    }

    private fun setupNavigationController() {
        val navigationController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navigationController, binding.drawerLayout)
    }
}
