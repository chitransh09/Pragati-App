package com.myprojects.pragati.model

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val isSchoolStudent: Boolean
)