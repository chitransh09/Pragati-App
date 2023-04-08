package com.myprojects.pragati.activities

import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myprojects.pragati.api.ApiService
import com.myprojects.pragati.databinding.ActivityLoginBinding
import com.myprojects.pragati.model.LoginRequest
import com.myprojects.pragati.model.LoginResponse
import com.myprojects.pragati.utils.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager(this)
        if (sharedPrefManager.getToken() != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(binding.root)

            binding.btnLogin.setOnClickListener {
                if (binding.edtTxtEmail2.text.toString().isEmpty()) {
                    binding.edtTxtEmail2.error = "Email Required"
                    binding.edtTxtEmail2.requestFocus()
                    return@setOnClickListener
                }else if (!binding.edtTxtEmail2.text.toString().contains("@")){
                    binding.edtTxtEmail2.error = "Email is invalid!!"
                    binding.edtTxtEmail2.requestFocus()
                    return@setOnClickListener
                }

                if (binding.edtTxtPassword2.text.toString().isEmpty()) {
                    binding.edtTxtPassword2.error = "Password Required"
                    binding.edtTxtPassword2.requestFocus()
                    return@setOnClickListener
                } else if (binding.edtTxtPassword2.length() < 8) {
                    binding.edtTxtPassword2.error = "Password must be minimum 8 characters"
                    binding.edtTxtPassword2.requestFocus()
                    return@setOnClickListener }
                else{
                    sendLoginData()
                }
            }

            binding.txtSignUp.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun sendLoginData() {

        // Create a Retrofit instance with the base URL
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

// Create an instance of the ApiService interface
        val apiService = retrofit.create(ApiService::class.java)

// Create a LoginRequest object with the user's email and password
        val loginUser = LoginRequest(binding.edtTxtEmail2.text.toString().trim(),binding.edtTxtPassword2.text.toString().trim())

// Make a POST request to the /user/login endpoint with the login request data
        val call = apiService.logIn(loginUser)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Login successful, extract token and start main activity
                        val token = loginResponse.token
                        val name = loginResponse.user.name
                        val email = loginResponse.user.email
                        if (isValidToken(token)) {
                            sharedPrefManager.saveToken(token)
                            sharedPrefManager.saveName(name)
                            sharedPrefManager.saveEmail(email)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid token", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    // Handle error
//                    val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), LoginResponse::class.java)
//                    Toast.makeText(applicationContext, errorResponse?.message ?: "Invalid email or password", Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext,"Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle error
//                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                Toast.makeText(this@LoginActivity, "Server Not Response", Toast.LENGTH_SHORT).show()
            }
        })


    }

    fun isValidToken(token: String): Boolean {
        // Check if token is not null or empty
        return token.isNotBlank()
    }

}