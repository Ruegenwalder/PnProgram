package com.example.pnprogram

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.pnprogram.databinding.ActivityMainBinding
import com.example.pnprogram.ui.inventory.InventoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // FAB functionality based on fragment
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.appBarMain.fab.setOnClickListener { view ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)

            if (currentFragment is InventoryFragment) {
                // FAB functionality for InventoryFragment: Show a dialog to add an item to the inventory
                currentFragment.showAddItemDialog()
            } else {
                // FAB functionality for other fragments (if any)
                Snackbar.make(view, "You clicked FAB in another fragment!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab)
                    .show()
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_summary, R.id.nav_character, R.id.nav_inventory, R.id.nav_notepad
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Update FAB icon dynamically based on fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_inventory -> {
                    binding.appBarMain.fab.setImageResource(R.drawable.add) // Set FAB icon for InventoryFragment
                }
                else -> {
                    binding.appBarMain.fab.setImageResource(R.drawable.add) // Set other icons for different fragments
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.main, menu)

        // Get the current destination fragment from the NavController
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val currentDestination = navController.currentDestination?.id

        // Hide the Settings menu item if we're already in the SettingsFragment
        if (currentDestination == R.id.nav_settings) {
            menu?.findItem(R.id.action_settings)?.isVisible = false
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                val currentDestination = navController.currentDestination?.id

                if (currentDestination == R.id.nav_settings) {
                    return true
                }

                val actionId = when (currentDestination) {
                    R.id.nav_summary -> R.id.action_summary_to_settings
                    R.id.nav_character -> R.id.action_character_to_settings
                    R.id.nav_inventory -> R.id.action_inventory_to_settings
                    R.id.nav_notepad -> R.id.action_notepad_to_settings
                    else -> R.id.action_summary_to_settings
                }
                navigateToFragment(actionId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToFragment(actionId: Int) {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(actionId)
    }
}
