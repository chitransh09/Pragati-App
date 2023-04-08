package com.myprojects.pragati.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.myprojects.pragati.R
import com.myprojects.pragati.adapters.DataScienceAndMlAdapter
import com.myprojects.pragati.adapters.FreelancingAdapter
import com.myprojects.pragati.databinding.FragmentDataScienceMlBinding
import com.myprojects.pragati.databinding.FragmentDataStructureBinding
import com.myprojects.pragati.databinding.FragmentFreelancingBinding
import com.myprojects.pragati.model.Websites

class FreelancingFragment : Fragment() {

    private var _binding: FragmentFreelancingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FreelancingAdapter
    private lateinit var progressBar: ProgressBar
    private val dataLists = mutableListOf<Websites>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFreelancingBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.freelancingToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar!!.title = "Freelancing"

        // Remove the app name from the Toolbar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.freelancingToolbar.navigationIcon?.setTintList(null) // Set the toolbar navigation icon to use the default color

        setHasOptionsMenu(true)

        adapter = FreelancingAdapter(dataLists) // Initialize RecyclerView adapter

        binding.freelancingRecyclerView.adapter = adapter  // Set RecyclerView adapter

        binding.freelancingRecyclerView.layoutManager = LinearLayoutManager(context)  // Set RecyclerView layout manager

        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        fetchData()

        return binding.root
    }

    private fun fetchData() {

        val db = Firebase.firestore
        val documentRef = db.collection("websites").document("freelancing").collection("items")
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                progressBar.visibility = View.GONE
                for (document in documentSnapshot) {
                    val title = document.getString("title")
                    val description = document.getString("description")
                    val link = document.getString("link")
                    val imageRef = document.getString("image")

                    if (title != null && description != null && imageRef != null) {

                        // Retrieve the image using Firebase Storage API

                        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageRef)
                        storageRef.downloadUrl.addOnSuccessListener { uri ->

                            // Add the retrieved data to the list
                            dataLists.add(Websites(id,title, description, uri.toString(), link))

                            // Notify adapter of data change
                            adapter.notifyDataSetChanged()
                        }.addOnFailureListener { exception ->
                            Log.w(ContentValues.TAG, "Error getting image URL.", exception)
                            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                progressBar.visibility = View.GONE
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
                Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_LONG).show()
            }
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