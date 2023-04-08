package com.myprojects.pragati.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.myprojects.pragati.R
import com.myprojects.pragati.databinding.FragmentDataStructureBinding
import com.myprojects.pragati.databinding.FragmentFreelancingBinding
import com.myprojects.pragati.databinding.FragmentHigherStudyBinding

class HigherStudyFragment : Fragment() {

    private var _binding: FragmentHigherStudyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHigherStudyBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.higherStudylToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar!!.title = "Higher Study"

        // Remove the app name from the Toolbar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.higherStudylToolbar.navigationIcon?.setTintList(null) // Set the toolbar navigation icon to use the default color

        setHasOptionsMenu(true)

        binding.higherStudyCard1.setOnClickListener {
            val gateFragment = GateFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, gateFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.higherStudyCard2.setOnClickListener {
            val catFragment = CatFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, catFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

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