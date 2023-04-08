package com.myprojects.pragati.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.pragati.databinding.EachTodoItemBinding
import com.myprojects.pragati.model.TodoItem

class TodoAdapter(private val items: List<TodoItem>) : RecyclerView.Adapter<TodoAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = items[position]
        holder.binding.todoTitle.text = item.todoTitle
        holder.binding.todoDescription.text = item.todoDescription
    }
}