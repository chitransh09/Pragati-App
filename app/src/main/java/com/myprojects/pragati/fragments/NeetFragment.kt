package com.myprojects.pragati.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.myprojects.pragati.R
import com.myprojects.pragati.databinding.FragmentNeetBinding
import com.myprojects.pragati.databinding.FragmentWebDevBinding

class NeetFragment : Fragment() {

    private var _binding: FragmentNeetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNeetBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.neetToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar!!.title = "Neet"

        // Remove the app name from the Toolbar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.neetToolbar.navigationIcon?.setTintList(null) // Set the toolbar navigation icon to use the default color

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}