package com.example.pnprogram.ui.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pnprogram.databinding.FragmentInventoryBinding

class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var inventoryViewModel: InventoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get the ViewModel
        inventoryViewModel =
            ViewModelProvider(this).get(InventoryViewModel::class.java)

        // Inflate the layout and set up binding
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe the list of items in the inventory
        val inventoryTextView: TextView = binding.textInventory
        inventoryViewModel.items.observe(viewLifecycleOwner) { items ->
            inventoryTextView.text = items.joinToString("\n") // Join items by newline
        }

        // Set up the FAB click listener to show the dialog
        binding.fab.setOnClickListener {
            showAddItemDialog()
        }

        return root
    }

    // Method to show the dialog to add an item
    private fun showAddItemDialog() {
        // Create a dialog to input the item name
        val input = EditText(requireContext())
        input.hint = "Enter item name"

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Item")
            .setMessage("Please enter the name of the item you want to add.")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val itemName = input.text.toString()
                if (itemName.isNotBlank()) {
                    // Add the item to the ViewModel
                    inventoryViewModel.addItem(itemName)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu() // Forces the menu to be recreated
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}