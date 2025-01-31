package com.example.pnprogram.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pnprogram.databinding.FragmentSummaryBinding

class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val summaryViewModel =
            ViewModelProvider(this).get(SummaryViewModel::class.java)

        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSummary
        summaryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
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