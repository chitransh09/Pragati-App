package com.myprojects.pragati.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myprojects.pragati.R
import com.myprojects.pragati.activities.WebViewActivity
import com.myprojects.pragati.databinding.RecyclerviewSingleCustomLinksItemBinding
import com.myprojects.pragati.model.CustomLinks

class CustomLinksAdapter(var items: List<CustomLinks>) :
    RecyclerView.Adapter<CustomLinksAdapter.CustomLinksViewHolder>() {

    class CustomLinksViewHolder(val binding: RecyclerviewSingleCustomLinksItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomLinksViewHolder {
        val binding = RecyclerviewSingleCustomLinksItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomLinksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomLinksViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtCustomLinksTitle.text = item.title
        Glide.with(holder.itemView.context)
            .load(item.favIconUrl)
            .placeholder(R.drawable.freelancing) // replace with your placeholder image
            .into(holder.binding.imgRecyclerViewCustomLinks)

        // Set an OnClickListener on the itemView to open the link
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", item.url)
//            intent.putExtra("title", item.title)
            holder.itemView.context.startActivity(intent)
        }
    }

}