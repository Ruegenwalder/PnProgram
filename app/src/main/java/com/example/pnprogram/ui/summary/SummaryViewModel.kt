package com.example.pnprogram.ui.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SummaryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the summary fragment"
    }
    val text: LiveData<String> = _text
}