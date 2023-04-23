package com.myprojects.pragati.adapters

import SharedPrefManager
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.pragati.R
import com.myprojects.pragati.activities.WebViewActivity
import com.myprojects.pragati.databinding.RecyclerviewSingleItemBinding
import com.myprojects.pragati.model.Websites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DataScienceAndMlAdapter(private val items: List<Websites>) :
    RecyclerView.Adapter<DataScienceAndMlAdapter.DataScienceAndMLViewHolder>() {

    class DataScienceAndMLViewHolder(val binding: RecyclerviewSingleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataScienceAndMLViewHolder {
        val binding = RecyclerviewSingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataScienceAndMLViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size    }

    override fun onBindViewHolder(holder: DataScienceAndMLViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtRecyclerViewTitle.text = item.title
        holder.binding.txtRecyclerViewDesc.text = item.description
        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.freelancing) // replace with your placeholder image
            .into(holder.binding.imgRecyclerView)

        //Holding favourite button
        GlobalScope.launch {
            try {
                val email = SharedPrefManager(holder.itemView.context).getEmail() //
                val favoritesRef =
                    Firebase.firestore.collection("favourites").document("userEmails")
                        .collection(email!!)
                val querySnapshot = favoritesRef
                    .whereEqualTo("title", item.title)
                    .whereEqualTo("link", item.link)
                    .get()
                    .await()
                val isAlreadyFavorite = !querySnapshot.isEmpty

                // update the UI to show the item as favorite if it's already present in the collection
                withContext(Dispatchers.Main) {
                    if (isAlreadyFavorite) {
                        holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_fill)
                    } else {
                        holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_blank)
                    }
                }
            } catch (e: Exception) {
                Log.e("Firestore", "Error checking favorites", e)
            }
        }

        // Add an OnClickListener to the favorite button
        holder.binding.btnFavorite.setOnClickListener {
            GlobalScope.launch {
                try {
                    val email = SharedPrefManager(holder.itemView.context).getEmail()
                    val favoritesRef =
                        Firebase.firestore.collection("favourites").document("userEmails")
                            .collection(email!!)
                    val querySnapshot = favoritesRef
                        .whereEqualTo("title", item.title)
                        .whereEqualTo("link", item.link)
                        .get()
                        .await()
                    val isAlreadyFavorite = !querySnapshot.isEmpty

                    if (!isAlreadyFavorite) {
                        // add the item to the list of favorites
                        val docRef = favoritesRef
                            .add(
                                mapOf(
                                    "title" to item.title,
                                    "link" to item.link,
                                    "image" to item.image,
                                    "description" to item.description,
                                    "filter" to "DS&ML" // add the new field "filter" here
                                )
                            )
                            .await()
                        Log.d("Firestore", "Document written with ID: ${docRef.id}")

                        // update the UI to show the item as favorite
                        withContext(Dispatchers.Main) {
                            holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_fill)
                        }
                    } else {
                        // remove the item from the list of favorites
                        val docSnapshot = querySnapshot.documents.first()
                        docSnapshot.reference.delete().await()
                        Log.d("Firestore", "Document deleted with ID: ${docSnapshot.id}")

                        // update the UI to show the item as not favorite
                        withContext(Dispatchers.Main) {
                            holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite_blank)
                        }

                    }
                } catch (e: Exception) {
                    Log.e("Firestore", "Error adding to favorites", e)
                }
            }
        }

        // Set an OnClickListener on the itemView to open the link
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, WebViewActivity::class.java)
            intent.putExtra("url", item.link)
            intent.putExtra("title", item.title)
            holder.itemView.context.startActivity(intent)
        }
    }
}