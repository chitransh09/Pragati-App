package com.myprojects.pragati.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.myprojects.pragati.R
import com.myprojects.pragati.adapters.FavoritesAdapter
import com.myprojects.pragati.databinding.FragmentFavoritesBinding
import com.myprojects.pragati.model.Websites

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoritesAdapter
    private val dataLists = mutableListOf<Websites>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.favoriteToolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        (activity as AppCompatActivity).supportActionBar!!.title = "Favorites"

        // Remove the app name from the Toolbar
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.favoriteToolbar.navigationIcon?.setTintList(null) // Set the toolbar navigation icon to use the default color

        setHasOptionsMenu(true)

        adapter = FavoritesAdapter(dataLists) // Initialize RecyclerView

        binding.favoriteRecyclerView.adapter = adapter  // Set RecyclerView adapter


        binding.favoriteRecyclerView.layoutManager =
            LinearLayoutManager(context)  // Set RecyclerView layout manager

        fetchData()

        return binding.root
    }

    private fun fetchData() {
        val sharedPrefs = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        for (item in getFavoriteWebsites()) {
            val isFavorite = sharedPrefs?.getBoolean("favorite_${item.id}", false) ?: false
            if (isFavorite) {
                dataLists.add(item)
            }
        }

        adapter.notifyDataSetChanged()
    }

    private fun getFavoriteWebsites(): List<Websites> {
        val sharedPreferences =
            requireActivity().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoriteIds = sharedPreferences.getStringSet("favoriteIds", setOf()) ?: setOf()
        val favoriteWebsites = mutableListOf<Websites>()
        for (id in favoriteIds) {
            val title = sharedPreferences.getString("favorite_${id}_title", null)
            val description = sharedPreferences.getString("favorite_${id}_description", null)
            val link = sharedPreferences.getString("favorite_${id}_link", null)
            val imageUrl = sharedPreferences.getString("favorite_${id}_imageUrl", null)
            if (title != null && description != null && link != null && imageUrl != null) {
                favoriteWebsites.add(Websites(id.toInt(), title, description, link, imageUrl))
            }
        }
        return favoriteWebsites
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}