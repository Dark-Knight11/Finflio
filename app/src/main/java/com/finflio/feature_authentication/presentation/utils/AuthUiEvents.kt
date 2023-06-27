package com.finflio.feature_authentication.presentation.utils

sealed class AuthUiEvents {
    data class ShowSnackbar(val message: String) : AuthUiEvents()
    object NavigateBack : AuthUiEvents()
}