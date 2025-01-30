package com.example.pnprogram.ui.notepad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotepadViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the notepad fragment"
    }
    val text: LiveData<String> = _text
}