package com.myprojects.pragati.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.pragati.adapters.CustomLinksAdapter
import com.myprojects.pragati.databinding.FragmentCustomLinkBinding
import com.myprojects.pragati.model.CustomLinks

class CustomLinkFragment : Fragment() {

    private lateinit var binding: FragmentCustomLinkBinding
    private lateinit var adapter: CustomLinksAdapter

    //    private lateinit var shimmerLayout: ShimmerFrameLayout
    private val dataLists = mutableListOf<CustomLinks>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomLinkBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.customLinksToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Remove the app name from the Toolbar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.customLinksToolbar.navigationIcon?.setTintList(null) // Set the toolbar navigation icon to use the default color

        setHasOptionsMenu(true)

        adapter = CustomLinksAdapter(dataLists) // Initialize RecyclerView adapter

        binding.customLinksRecyclerView.adapter = adapter  // Set RecyclerView adapter

        binding.customLinksRecyclerView.layoutManager =
            LinearLayoutManager(context)  // Set RecyclerView layout manager

        fetchData()

        return binding.root
    }

    private fun fetchData() {

        val db = Firebase.firestore
        db.collection("customLink")
            .get()
            .addOnSuccessListener { documents ->
                val items = mutableListOf<CustomLinks>()

                for (document in documents) {
                    val title = document.getString("title")
                    val url = document.getString("url")
                    val userEmail = document.getString("ownerEmail")
                    val imageRef = document.getString("favIconUrl")

                    if (title != null && url != null && imageRef != null) {
                        val item = CustomLinks(id, imageRef, userEmail, title, url)
                        items.add(item)
                    }
                }

                adapter.items = items
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
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