package com.example.pnprogram.ui.notepad

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pnprogram.R

class NotepadFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var addNoteButton: Button
    private lateinit var notesContainer: LinearLayout
    private lateinit var undoMessage: TextView
    private lateinit var undoAllButton: Button  // Button for undoing all deletions

    private val notepadViewModel: NotepadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_notepad, container, false)

        // Initialize views
        editText = rootView.findViewById(R.id.editText)
        addNoteButton = rootView.findViewById(R.id.addNoteButton)
        notesContainer = rootView.findViewById(R.id.notesContainer)
        undoMessage = rootView.findViewById(R.id.undoMessage)
        undoAllButton = rootView.findViewById(R.id.undoAllButton) // Initialize the undo all button

        // Load notes from ViewModel
        notepadViewModel.loadNotes(requireContext())

        // Observe changes to notes
        notepadViewModel.notes.observe(viewLifecycleOwner) { notes ->
            updateNotes(notes)
        }

        // Add note button listener
        addNoteButton.setOnClickListener {
            val noteText = editText.text.toString()
            if (noteText.isNotBlank()) {
                notepadViewModel.addNote(noteText)
                editText.text.clear() // Clear the input field after adding
                notepadViewModel.saveNotes() // Save notes after adding
            }
        }

        // Undo all button listener (undo multiple deletions)
        undoAllButton.setOnClickListener {
            notepadViewModel.undoAllDeletes() // Undo all deletions
            undoMessage.visibility = View.GONE // Hide the undo message
        }

        return rootView
    }

    // Update the UI with the latest notes
    private fun updateNotes(notes: List<String>) {
        notesContainer.removeAllViews() // Clear the existing notes

        notes.forEach { noteText ->
            val noteView = TextView(requireContext()).apply {
                text = noteText
                setPadding(16, 16, 16, 16)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f // Makes sure the note occupies all available space
                }
            }

            val deleteButton = Button(requireContext()).apply {
                text = getString(R.string.delete_button)
                setPadding(8, 8, 8, 8) // Adjust padding to make it smaller and fit better
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 0, 0, 0) // Ensure the delete button has space from the text
                }
                background = null // Reset the background (use a default button style or set a custom one)

                setOnClickListener {
                    notepadViewModel.deleteNote(noteText) // Delete the note
                    showUndoMessage() // Show undo message after deletion
                }
            }

            val noteContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(noteView)
                addView(deleteButton)
            }

            notesContainer.addView(noteContainer)
        }

        // Show Undo All button only if there are deleted notes
        undoAllButton.visibility = if (notepadViewModel._deletedNotes.value?.isNotEmpty() == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    // Show undo message after deletion
    private fun showUndoMessage() {
        undoMessage.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            undoMessage.visibility = View.GONE
        }, 7000)

        undoMessage.setOnClickListener {
            val restored = notepadViewModel.undoDelete() // Undo the delete action
            if (!restored) {
                undoMessage.visibility = View.GONE // Hide message if no deletion to undo
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu() // Forces the menu to be recreated
    }
}
