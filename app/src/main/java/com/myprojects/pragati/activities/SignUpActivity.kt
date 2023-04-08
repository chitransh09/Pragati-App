package com.myprojects.pragati.activities

import SharedPrefManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.myprojects.pragati.api.ApiService
import com.myprojects.pragati.databinding.ActivitySignUpBinding
import com.myprojects.pragati.model.SignUpRequest
import com.myprojects.pragati.model.SignUpResponse
import com.myprojects.pragati.utils.ConnectionManager
import com.myprojects.pragati.utils.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var apiService: ApiService
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var connectionManager: ConnectionManager
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false) // ye line testmode me hai
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager(this)

        connectionManager = ConnectionManager()
        checkInternetConnection(this)

        binding.btnSignUp.setOnClickListener {

            val fnm = binding.edtFirstName.text.toString().trim()
            val lnm = binding.edtLastName.text.toString().trim()
            val em = binding.edtTxtEmail.text.toString().trim()
            val pw = binding.edtTxtPassword.text.toString().trim()

            if (fnm.isEmpty()) {
                binding.edtFirstName.error = "First Name Required"
                binding.edtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (lnm.isEmpty()) {
                binding.edtLastName.error = "Last Name Required"
                binding.edtLastName.requestFocus()
                return@setOnClickListener
            }

            if (em.isEmpty()) {
                binding.edtTxtEmail.error = "Email Required"
                binding.edtTxtEmail.requestFocus()
                return@setOnClickListener
            }else if (!em.contains("@")){
                binding.edtTxtEmail.error = "Email is invalid!!"
                binding.edtTxtEmail.requestFocus()
                return@setOnClickListener
            }

            if (pw.isEmpty()) {
                binding.edtTxtPassword.error = "Password Required"
                binding.edtTxtPassword.requestFocus()
                return@setOnClickListener
            } else if (binding.edtTxtPassword.length() < 8) {
                binding.edtTxtPassword.error = "Password must be minimum 8 characters"
                binding.edtTxtPassword.requestFocus()
                return@setOnClickListener
            }

            sendSignUpData()
        }

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun sendSignUpData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val users = SignUpRequest(
            binding.edtFirstName.text.toString().trim(),
            binding.edtLastName.text.toString().trim(),
            binding.edtTxtEmail.text.toString().trim(),
            binding.edtTxtPassword.text.toString().trim(),
            false
        )

        val call = apiService.signUp(users)
        call.enqueue(object : Callback<SignUpResponse> { override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
            if (response.isSuccessful) {
                // Handle successful signup
                val responseBody = response.body()
                if (responseBody != null) {
                    // User was created successfully
                    val signUpResponse = response.body()
                    val token = signUpResponse!!.token
                    val name = signUpResponse.user.name
                    val email = signUpResponse.user.email
                    sharedPrefManager.saveToken(token)
                    sharedPrefManager.saveName(name)
                    sharedPrefManager.saveEmail(email)
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                // Handle signup error
//                val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), SignUpResponse::class.java)
//                Toast.makeText(applicationContext, errorResponse?.message ?: "Invalid email or password", Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext, "Fill up the correct credentials", Toast.LENGTH_SHORT).show()
            }
        }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun checkInternetConnection(activity: Activity) {
        if (!connectionManager.isInternetConnected(this)) {
            // Show an alert dialog if there is no internet connection
            val builder = AlertDialog.Builder(this)
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