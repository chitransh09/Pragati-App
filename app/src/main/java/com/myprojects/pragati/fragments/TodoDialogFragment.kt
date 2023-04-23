package com.myprojects.pragati.fragments

import SharedPrefManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.myprojects.pragati.databinding.FragmentTodoDialogBinding
import com.myprojects.pragati.model.TodoItem


class TodoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentTodoDialogBinding
    private var listener: OnDialogNextBtnClickListener? = null
    private var toDoData: TodoItem? = null
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "TodoDialogFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = TodoDialogFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
//                putString("time", time)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodoDialogBinding.inflate(inflater, container, false)

        sharedPrefManager = SharedPrefManager(requireContext())

        if (arguments != null) {
            toDoData = TodoItem(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString(),
//                arguments?.getString("time").toString()
            )

            binding.todoEt.setText(toDoData?.task)
        }

        createNewTodo()

        binding.todoClose.setOnClickListener {
            dismiss()
        }

        return binding.root

    }

    fun createNewTodo() {
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()) {

                if (toDoData == null) {
                    listener?.saveTask(todoTask, binding.todoEt)
                } else {
                    toDoData!!.task = todoTask
                    listener?.updateTask(toDoData!!, binding.todoEt)
                }

            } else {
                Toast.makeText(context, "Please type your task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface OnDialogNextBtnClickListener {
        fun saveTask(todoTask: String, todoEdit: TextInputEditText)
        fun updateTask(toDoData: TodoItem, todoEdit: TextInputEditText)
    }

}