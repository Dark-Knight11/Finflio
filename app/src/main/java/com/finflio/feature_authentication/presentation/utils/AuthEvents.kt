package com.finflio.feature_authentication.presentation.utils

sealed class AuthEvents {
    data class Login(val email: String, val password: String) : AuthEvents()
    data class Register(
        val name: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : AuthEvents()
}