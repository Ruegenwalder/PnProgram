package com.example.pnprogram.ui.notepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pnprogram.databinding.FragmentNotepadBinding

class NotepadFragment : Fragment() {

    private var _binding: FragmentNotepadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notepadViewModel =
            ViewModelProvider(this).get(NotepadViewModel::class.java)

        _binding = FragmentNotepadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotepad
        notepadViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}