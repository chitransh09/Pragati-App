package com.myprojects.pragati.model

data class SignUpResponse(
    val token: String,
    val user: User,
    val message: String?
)