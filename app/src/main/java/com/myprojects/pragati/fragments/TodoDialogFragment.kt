package com.myprojects.pragati.fragments

import SharedPrefManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.myprojects.pragati.R
import com.myprojects.pragati.databinding.FragmentTodoBinding
import com.myprojects.pragati.databinding.FragmentTodoDialogBinding
import com.myprojects.pragati.model.TodoItem


class TodoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentTodoDialogBinding
    private var listener : OnDialogNextBtnClickListener? = null
    private var toDoData: TodoItem? = null
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodoDialogBinding.inflate(inflater, container, false)

        sharedPrefManager = SharedPrefManager(requireContext())


        binding.todoNextBtn.setOnClickListener {
            createNewTodo()
        }

        binding.todoClose.setOnClickListener {
            dismiss()
        }

        return binding.root

    }

    private fun createNewTodo() {
        val todoTitle = binding.todoTitleEt.text.toString().trim()
        val todoDescription = binding.todoEt.text.toString().trim()

        if (todoTitle.isEmpty()) {
            binding.todoTitleEt.error = "Title is required"
            binding.todoEt.requestFocus()
            return
        }

        val currentUserEmail = sharedPrefManager.getEmail()
        if (currentUserEmail == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val todoItem = TodoItem(todoTitle, todoDescription, currentUserEmail)

        val database = Firebase.database.reference
        database.child("todos").child(currentUserEmail).push().setValue(todoItem)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "To-do item created successfully", Toast.LENGTH_SHORT).show()
                binding.todoTitleEt.text?.clear()
                binding.todoEt.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to create to-do item", Toast.LENGTH_SHORT).show()
            }
    }

    interface OnDialogNextBtnClickListener{
        fun saveTask(todoTask:String , todoEdit: TextInputEditText)
        fun updateTask(toDoData: TodoItem , todoEdit: TextInputEditText)
    }

}