package com.hse.education.data.network

data class RegisterUserRequest(
    val email: String,
    val password: String,
    val userName: String
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)