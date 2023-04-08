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

class FavoritesAdapter(private val items: List<Websites>) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_single_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.freelancing) // replace with your placeholder image
            .into(holder.imageView)

        // Add an OnClickListener to the favorite button
        holder.favoriteButton.setOnClickListener {
            val sharedPrefs = holder.itemView.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.remove("favorite_${item.id}")
            editor.remove("favorite_${item.id}").apply()
            notifyDataSetChanged()
        }

        // Set an OnClickListener on the itemView to open the link
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", item.link)
            holder.itemView.context.startActivity(intent)
        }
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtRecyclerViewTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtRecyclerViewDesc)
        val imageView: ImageView = itemView.findViewById(R.id.imgRecyclerView)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.btnFavorite)
    }
}
