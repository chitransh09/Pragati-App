package com.myprojects.pragati.fragments

import SharedPrefManager
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.myprojects.pragati.R
import com.myprojects.pragati.adapters.GateAdapter
import com.myprojects.pragati.adapters.TodoAdapter
import com.myprojects.pragati.databinding.FragmentSchoolBinding
import com.myprojects.pragati.databinding.FragmentTodoBinding
import com.myprojects.pragati.model.TodoItem
import com.myprojects.pragati.model.Websites

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TodoAdapter
    private lateinit var progressBar: ProgressBar
    private val dataLists = mutableListOf<TodoItem>()
    private val todoDialogFragment = TodoDialogFragment()
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        adapter = TodoAdapter(dataLists) // Initialize RecyclerView adapter

        binding.todoRecyclerView.adapter = adapter  // Set RecyclerView adapter

        binding.todoRecyclerView.layoutManager = LinearLayoutManager(context)  // Set RecyclerView layout manager

        todoDialogFragment.setListener(object : TodoDialogFragment.OnDialogNextBtnClickListener {
            override fun saveTask(todoTask: String, todoEdit: TextInputEditText) {
                // Handle the save action
            }

            override fun updateTask(toDoData: TodoItem, todoEdit: TextInputEditText) {
                // Handle the update action
            }
        })

// Set the onClickListener for the floating button
        binding.btnAddTodo.setOnClickListener {
            // Show the TodoDialogFragment
            todoDialogFragment.show(childFragmentManager, "todo_dialog")

        }

        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        return binding.root
    }

}