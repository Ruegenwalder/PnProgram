package com.example.pnprogram.ui.notepad

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class NotepadViewModel(application: Application) : AndroidViewModel(application) {

    private val _notes = MutableLiveData<MutableList<String>>()
    val notes: LiveData<MutableList<String>> get() = _notes

    internal val _deletedNotes = MutableLiveData<MutableList<String>>() // New list to hold deleted notes
    private val sharedPrefs = application.getSharedPreferences("notepad_prefs", Context.MODE_PRIVATE)

    init {
        loadNotes(application) // Load notes from SharedPreferences when the ViewModel is created
        _deletedNotes.value = mutableListOf() // Initialize deleted notes list
    }

    // Add a new note
    fun addNote(noteText: String) {
        val currentNotes = _notes.value ?: mutableListOf()
        currentNotes.add(noteText)
        _notes.value = currentNotes
        saveNotes()
    }

    // Delete a note
    fun deleteNote(noteText: String) {
        val currentNotes = _notes.value ?: mutableListOf()
        if (currentNotes.contains(noteText)) {
            val deletedNotes = _deletedNotes.value ?: mutableListOf()
            deletedNotes.add(noteText) // Save the deleted note
            _deletedNotes.value = deletedNotes
            currentNotes.remove(noteText)
            _notes.value = currentNotes
            saveNotes()
        }
    }

    // Undo the last delete action
    fun undoDelete(): Boolean {
        val deletedNotes = _deletedNotes.value ?: mutableListOf()
        if (deletedNotes.isNotEmpty()) {
            val noteToRestore = deletedNotes.removeAt(deletedNotes.size - 1)
            _deletedNotes.value = deletedNotes

            val currentNotes = _notes.value ?: mutableListOf()
            currentNotes.add(noteToRestore) // Restore the deleted note
            _notes.value = currentNotes
            saveNotes()
            return true
        }
        return false
    }

    // Undo all delete actions
    fun undoAllDeletes() {
        val deletedNotes = _deletedNotes.value ?: mutableListOf()
        val currentNotes = _notes.value ?: mutableListOf()
        currentNotes.addAll(deletedNotes) // Restore all deleted notes
        _notes.value = currentNotes
        _deletedNotes.value = mutableListOf() // Clear deleted notes list
        saveNotes()
    }

    // Save notes to SharedPreferences
    internal fun saveNotes() {
        val notesJson = Gson().toJson(_notes.value)
        val deletedNotesJson = Gson().toJson(_deletedNotes.value)
        sharedPrefs.edit()
            .putString("notes", notesJson)
            .putString("deletedNotes", deletedNotesJson)
            .apply()
    }

    // Load notes from SharedPreferences
    fun loadNotes(context: Context) {
        val notesJson = sharedPrefs.getString("notes", "[]") ?: "[]"
        val deletedNotesJson = sharedPrefs.getString("deletedNotes", "[]") ?: "[]"

        val type: Type = object : TypeToken<MutableList<String>>() {}.type
        val savedNotes: MutableList<String> = Gson().fromJson(notesJson, type)
        val savedDeletedNotes: MutableList<String> = Gson().fromJson(deletedNotesJson, type)

        _notes.value = savedNotes
        _deletedNotes.value = savedDeletedNotes
    }
}