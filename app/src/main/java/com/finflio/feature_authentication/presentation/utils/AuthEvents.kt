package com.finflio.feature_authentication.presentation.utils

sealed class AuthEvents {
    data class Login(val email: String, val password: String) : AuthEvents()
    object Register : AuthEvents()
}