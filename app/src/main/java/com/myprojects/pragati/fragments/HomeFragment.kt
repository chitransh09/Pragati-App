package com.myprojects.pragati.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.myprojects.pragati.R
import com.myprojects.pragati.databinding.FragmentHomeBinding
import com.myprojects.pragati.utils.ConnectionManager

class HomeFragment : Fragment() {

    private lateinit var connectionManager: ConnectionManager
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val binding = FragmentHomeBinding.bind(view)

        connectionManager = ConnectionManager()
        checkInternetConnection(requireActivity())

        // Retrieve the first name from the arguments
        val Name = arguments?.getString("name")
        val firstName = Name.toString().split(" ")[0]

        // Display the first name in the UI
        binding.txtHey.text = "Hey, $firstName !"

        binding.card1.setOnClickListener {
            val schoolFragment = SchoolFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, schoolFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.card2.setOnClickListener {
            val dataStructureFragment = DataStructureFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, dataStructureFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.card3.setOnClickListener {
            val webDevFragment = WebDevFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, webDevFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.card4.setOnClickListener {
            val dataScienceMLFragment = DataScienceMLFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, dataScienceMLFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.card5.setOnClickListener {
            val freelancingFragment = FreelancingFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, freelancingFragment)
                ?.addToBackStack(null)
                ?.commit()
        }


        binding.card6.setOnClickListener {
            val higherStudyFragment = HigherStudyFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, higherStudyFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.card7.setOnClickListener {
            val aptitudeFragment = AptitudeFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame, aptitudeFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }

    private fun checkInternetConnection(activity: Activity) {
        if (!connectionManager.isInternetConnected(requireActivity())) {
            // Show an alert dialog if there is no internet connection
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage("Please turn on your internet connection to continue")
                .setCancelable(false)
                .setPositiveButton("Open Network Settings") { _, _ ->
                    activity.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .setNegativeButton("Exit") { _, _ ->
                    activity.finishAffinity()
                }
            alertDialog = builder.create()
            alertDialog.show()
        }
    }

}