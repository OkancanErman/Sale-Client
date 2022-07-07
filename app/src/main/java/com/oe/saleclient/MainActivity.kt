package com.oe.saleclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.oe.saleclient.databinding.ActivityMainBinding
import com.oe.saleclient.fragment.ItemsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.itemsFragment ->{
                    binding.fab.show()
                    binding.bottomAppBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.fab.hide()
                    binding.bottomAppBar.visibility = View.GONE
                }
            }
        }

        binding.bottomNav.background = null

        val exitDialog = AlertDialog.Builder(this)
            .setTitle("EXIT")
            .setMessage("Do you want to exit?")
            .setIcon(R.drawable.ic_baseline_power_settings_new_24)
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .create()

        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.exitApp -> {
                    exitDialog.show()
                }
            }
            true
        }

        binding.bottomNav.setOnItemReselectedListener { item ->
            when(item.itemId) {
                R.id.exitApp -> {
                exitDialog.show()
            }
        }
            return@setOnItemReselectedListener
        }

        binding.fab.setOnClickListener {
            val action = ItemsFragmentDirections.actionItemsFragmentToAddItemFragment()
            navController.navigate(action)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}