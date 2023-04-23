package com.myprojects.pragati.fragments

import SharedPrefManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myprojects.pragati.adapters.TodoAdapter
import com.myprojects.pragati.databinding.FragmentTodoBinding
import com.myprojects.pragati.model.TodoItem

class TodoFragment : Fragment(), TodoDialogFragment.OnDialogNextBtnClickListener,
    TodoAdapter.TodoAdapterClicksInterface {

    private lateinit var binding: FragmentTodoBinding

    //    private val binding get() = _binding
    private lateinit var adapter: TodoAdapter
    private lateinit var progressBar: ProgressBar
    private var dataLists: MutableList<TodoItem> = mutableListOf()
    private lateinit var databaseRef: DatabaseReference
    private var todoDialogFragment: TodoDialogFragment? = null
    private lateinit var sharedPrefManager: SharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodoBinding.inflate(inflater, container, false)

        adapter = TodoAdapter(dataLists) // Initialize RecyclerView adapter
        adapter.setListener(this)

        binding.todoRecyclerView.adapter = adapter  // Set RecyclerView adapter

        binding.todoRecyclerView.setHasFixedSize(true)

        binding.todoRecyclerView.layoutManager =
            LinearLayoutManager(context)  // Set RecyclerView layout manager

        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        sharedPrefManager = SharedPrefManager(requireContext())

        sendTask()
        getDataFromFirebase()

        return binding.root
    }

    private fun sendTask() {
        binding.btnAddTodo.setOnClickListener {
            if (todoDialogFragment != null) {
                childFragmentManager.beginTransaction().remove(todoDialogFragment!!).commit()
            }
            todoDialogFragment = TodoDialogFragment()
            todoDialogFragment!!.setListener(this)
            todoDialogFragment!!.show(
                childFragmentManager,
                TodoDialogFragment.TAG
            )

        }
    }

    private fun getDataFromFirebase() {
        val currentUserEmail = sharedPrefManager.getEmail()
        if (currentUserEmail == null) {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val encodedEmail = currentUserEmail.toString().replace(".", ",")
            .replace("@", "_")

        databaseRef = FirebaseDatabase.getInstance().reference.child("todos").child(encodedEmail)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                dataLists.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask =
                        taskSnapshot.key?.let { TodoItem(it, taskSnapshot.value.toString()) }

                    if (todoTask != null) {
                        dataLists.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun saveTask(todoTask: String, todoEdit: TextInputEditText) {
        val currentUserEmail = sharedPrefManager.getEmail()
        if (currentUserEmail == null) {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val encodedEmail = currentUserEmail.toString().replace(".", ",")
            .replace("@", "_")

        databaseRef = FirebaseDatabase.getInstance().reference.child("todos").child(encodedEmail)

        databaseRef.push().setValue(todoTask).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Todo saved successfully !!", Toast.LENGTH_SHORT).show()
                todoEdit.text = null
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            todoDialogFragment!!.dismiss()
        }

    }

    override fun updateTask(toDoData: TodoItem, todoEdit: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully🫡", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEdit.text = null
            todoDialogFragment!!.dismiss()
        }
    }


    override fun onDeleteTaskBtnClicked(todoItem: TodoItem, position: Int) {
        databaseRef.child(todoItem.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully !!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    override fun onEditTaskBtnClicked(todoItem: TodoItem, position: Int) {
//        if (todoDialogFragment != null){
//            childFragmentManager.beginTransaction().remove(todoDialogFragment!!).commit()
//
//            todoDialogFragment = TodoDialogFragment.newInstance(todoItem.taskId , todoItem.task)
//            todoDialogFragment!!.setListener(this)
//            todoDialogFragment!!.show(childFragmentManager , TodoDialogFragment.TAG)
//        }
//    }

    override fun onEditTaskBtnClicked(todoItem: TodoItem, position: Int) {
        todoDialogFragment = TodoDialogFragment.newInstance(todoItem.taskId, todoItem.task)
        todoDialogFragment!!.setListener(this)
        todoDialogFragment!!.show(childFragmentManager, TodoDialogFragment.TAG)
    }

}
