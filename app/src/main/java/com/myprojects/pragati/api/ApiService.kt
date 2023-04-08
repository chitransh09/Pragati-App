package com.myprojects.pragati.api

import com.myprojects.pragati.model.LoginRequest
import com.myprojects.pragati.model.LoginResponse
import com.myprojects.pragati.model.SignUpRequest
import com.myprojects.pragati.model.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("user/signup")
    fun signUp(@Body signupRequest: SignUpRequest): Call<SignUpResponse>

    @POST("user/login")
    fun logIn(@Body loginRequest: LoginRequest): Call<LoginResponse>
}