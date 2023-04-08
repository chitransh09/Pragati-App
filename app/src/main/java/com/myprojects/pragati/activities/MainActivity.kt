package com.myprojects.pragati.activities

import SharedPrefManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.myprojects.pragati.R
import com.myprojects.pragati.databinding.ActivityMainBinding
import com.myprojects.pragati.fragments.HomeFragment
import com.myprojects.pragati.fragments.MoreFragment
import com.myprojects.pragati.fragments.TodoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPrefManager = SharedPrefManager(applicationContext)
        val name = sharedPrefManager.getName()
        val email = sharedPrefManager.getEmail()
        //Set Default fragment
        showFragment(HomeFragment().apply {
            arguments = Bundle().apply {
                putString("name", name)
            }
        })


        binding.bottomNavMenu.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> {
                    showFragment(HomeFragment().apply {
                        arguments = Bundle().apply {
                            putString("name", name)
                        }
                    })
                }
                R.id.todo -> showFragment(TodoFragment())
                R.id.more -> {
                    showFragment(MoreFragment().apply {
                        arguments = Bundle().apply {
                            putString("name", name)
                            putString("email", email)
                        }
                    })
                }
                else -> false
            }
            return@setOnItemSelectedListener true
        }

    }

    private fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.addToBackStack("Home Fragment")
        transaction.commit()
    }
}