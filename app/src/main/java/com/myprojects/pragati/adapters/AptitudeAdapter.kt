package com.myprojects.pragati.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.myprojects.pragati.R
import com.myprojects.pragati.activities.WebViewActivity
import com.myprojects.pragati.databinding.RecyclerviewSingleItemBinding
import com.myprojects.pragati.model.Websites

class AptitudeAdapter(private val items: List<Websites>) : RecyclerView.Adapter<AptitudeAdapter.AptitudeViewHolder>() {

    class AptitudeViewHolder(val binding: RecyclerviewSingleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AptitudeViewHolder {
        val binding = RecyclerviewSingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AptitudeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AptitudeViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtRecyclerViewTitle.text = item.title
        holder.binding.txtRecyclerViewDesc.text = item.description
        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.freelancing) // replace with your placeholder image
            .into(holder.binding.imgRecyclerView)

        // Add an OnClickListener to the favorite button
        val sharedPrefs = holder.itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        holder.binding.btnFavorite.setOnClickListener {
            val isFavorite = sharedPrefs.getBoolean("favorite_${item.id}", false)
            val editor = sharedPrefs.edit()
            editor.apply()
            if (isFavorite) {
                editor.remove("favorite_${item.id}")
                holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_blank)
            } else {
                editor.putBoolean("favorite_${item.id}", true)
                holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_fill)
            }
            editor.apply()

            // Add/remove item ID to/from Firestore favorites collection for current user
            val userId = sharedPrefs.getString("userId", "")
            if (!userId.isNullOrEmpty()) {
                val firestore = FirebaseFirestore.getInstance()
                val userRef = firestore.collection("users").document(userId)
                val favoritesRef = userRef.collection("favorites")

                if (isFavorite) {
                    favoritesRef.document(item.id.toString()).delete()
                } else {
                    favoritesRef.document(item.id.toString()).set(item)
                }
            }
        }

        // Set an OnClickListener on the itemView to open the link
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", item.link)
            holder.itemView.context.startActivity(intent)
        }
    }

}