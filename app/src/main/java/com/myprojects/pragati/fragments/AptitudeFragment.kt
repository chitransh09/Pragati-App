package com.myprojects.pragati.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.myprojects.pragati.adapters.AptitudeAdapter
import com.myprojects.pragati.databinding.FragmentAptitudeBinding
import com.myprojects.pragati.model.Websites

class AptitudeFragment : Fragment() {

    private var _binding: FragmentAptitudeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AptitudeAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private val dataLists = mutableListOf<Websites>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAptitudeBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.aptitudeToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Remove the app name from the Toolbar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.aptitudeToolbar.navigationIcon?.setTintList(null) // Set the toolbar navigation icon to use the default color

        setHasOptionsMenu(true)

        adapter = AptitudeAdapter(dataLists) // Initialize RecyclerView adapter

        binding.aptitudeRecyclerView.adapter = adapter  // Set RecyclerView adapter

        binding.aptitudeRecyclerView.layoutManager =
            LinearLayoutManager(context)  // Set RecyclerView layout manager

        shimmerLayout = binding.shimmerView
        shimmerLayout.visibility = View.VISIBLE

//        progressBar = binding.progressBar
//        progressBar.visibility = View.VISIBLE

        fetchData()

        return binding.root
    }

    private fun fetchData() {

        val db = Firebase.firestore
        val documentRef = db.collection("websites").document("aptitude").collection("items")
        val query: Task<QuerySnapshot> = documentRef.get()
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                shimmerLayout.visibility = View.GONE
//                progressBar.visibility = View.GONE
                for (document in documentSnapshot) {
                    val itemId = document.id
//                    val itemData = document.data
                    Log.d("Firestore", "Item ID: $itemId")
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
                shimmerLayout.visibility = View.VISIBLE
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