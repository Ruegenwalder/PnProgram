package com.example.pnprogram.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InventoryViewModel : ViewModel() {

    // List of items in the inventory
    private val _items = MutableLiveData<MutableList<String>>(mutableListOf())
    val items: LiveData<MutableList<String>> get() = _items

    // Add a new item to the inventory
    fun addItem(item: String) {
        val currentItems = _items.value ?: mutableListOf()
        currentItems.add(item)
        _items.value = currentItems
    }

    // Optionally: Clear all items (if needed for reset functionality)
    fun clearItems() {
        _items.value = mutableListOf()
    }
}