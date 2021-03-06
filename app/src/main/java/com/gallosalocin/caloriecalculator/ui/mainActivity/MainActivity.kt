package com.gallosalocin.caloriecalculator.ui.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.gallosalocin.caloriecalculator.R
import com.gallosalocin.caloriecalculator.databinding.ActivityMainBinding
import com.gallosalocin.caloriecalculator.utils.Constants.DAY_TAG_NO_CHOICE
import com.gallosalocin.caloriecalculator.utils.Constants.GLOBAL_CHOICE_NO_CHOICE
import com.gallosalocin.caloriecalculator.utils.Constants.MEAL_TAG_NO_CHOICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    companion object {
        var dayTag = DAY_TAG_NO_CHOICE
        var mealTag = MEAL_TAG_NO_CHOICE
        var globalChoice = GLOBAL_CHOICE_NO_CHOICE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.dayFragment))

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}