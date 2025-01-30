package com.example.pnprogram.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InventoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the inventory fragment"
    }
    val text: LiveData<String> = _text
}