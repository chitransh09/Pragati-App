package com.myprojects.pragati.model

data class LoginResponse(
    val token: String,
    val user: User,
    val message: String
)