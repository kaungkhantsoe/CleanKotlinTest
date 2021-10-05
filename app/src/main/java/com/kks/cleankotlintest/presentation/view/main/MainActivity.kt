package com.kks.cleankotlintest.presentation.view.main

import android.view.LayoutInflater
import com.kks.cleankotlintest.R
import com.kks.cleankotlintest.databinding.ActivityMainBinding
import com.kks.cleankotlintest.common.*
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController


class MainActivity : ViewBindingActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setup() {

        val navController = findNavController(R.id.nav_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

}