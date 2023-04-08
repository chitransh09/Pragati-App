package com.myprojects.pragati.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myprojects.pragati.R
import com.myprojects.pragati.activities.WebViewActivity
import com.myprojects.pragati.model.Websites

class DataStructureAdapter(private val items: List<Websites>) :
    RecyclerView.Adapter<DataStructureAdapter.DataStructureViewHolder>() {

    class DataStructureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtRecyclerViewTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtRecyclerViewDesc)
        val imageView: ImageView = itemView.findViewById(R.id.imgRecyclerView)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataStructureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_single_item, parent, false)
        return DataStructureViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DataStructureViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.freelancing) // replace with your placeholder image
            .into(holder.imageView)

        // Add an OnClickListener to the favorite button
        val sharedPrefs = holder.itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        holder.favoriteButton.setOnClickListener {
            val isFavorite = sharedPrefs.getBoolean("favorite_${item.id}", false)
            val editor = sharedPrefs.edit()
            if (isFavorite) {
                editor.remove("favorite_${item.id}")
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_blank)
            } else {
                editor.putBoolean("favorite_${item.id}", true)
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_fill)
            }
            editor.apply()
        }

        // Set an OnClickListener on the itemView to open the link
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", item.link)
            holder.itemView.context.startActivity(intent)
        }
    }
}