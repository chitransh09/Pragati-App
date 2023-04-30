package com.myprojects.pragati.fragments

import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.myprojects.pragati.R
import com.myprojects.pragati.activities.LoginActivity
import com.myprojects.pragati.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!
    lateinit var logOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        // Retrieve the name and email arguments
        val name = arguments?.getString("name")
        binding.txtInputName.text = "$name"
        val email = arguments?.getString("email")
        binding.txtInputEmail.text = "$email"


        binding.logOut.setOnClickListener {
            val sharedPrefManager = SharedPrefManager(requireContext())
            sharedPrefManager.deleteToken()

            // Redirect to login screen
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.aboutUsCard.setOnClickListener {
            val fragment = AboutUsFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

//        binding.faqCard.setOnClickListener {
//            val fragment = FaqsFragment()
//            val transaction = fragmentManager?.beginTransaction()
//            transaction?.replace(R.id.frame, fragment)
//            transaction?.addToBackStack(null)
//            transaction?.commit()
//        }

        binding.favoriteCard.setOnClickListener {
            val fragment = FavoritesFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        binding.customLinkCard.setOnClickListener {
            val fragment = CustomLinkFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        return binding.root
    }

}