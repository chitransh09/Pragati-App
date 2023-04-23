package com.myprojects.pragati.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.pragati.databinding.EachTodoItemBinding
import com.myprojects.pragati.fragments.TodoDialogFragment.Companion.TAG
import com.myprojects.pragati.model.TodoItem

class TodoAdapter(private val items: MutableList<TodoItem>) :
    RecyclerView.Adapter<TodoAdapter.TaskViewHolder>() {

    private var listener: TodoAdapterClicksInterface? = null
    fun setListener(listener: TodoAdapterClicksInterface) {
        this.listener = listener
    }

    class TaskViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.todoDescription.text = this.task

//                val dateFormat = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault())
//                binding.todoDate.text = dateFormat.format(this.time)

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this, position)
                }

                Log.d(TAG, "onBindViewHolder: " + this)
                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this, position)
                }


            }
        }
    }

    interface TodoAdapterClicksInterface {
        fun onDeleteTaskBtnClicked(todoItem: TodoItem, position: Int)
        fun onEditTaskBtnClicked(todoItem: TodoItem, position: Int)
    }
}