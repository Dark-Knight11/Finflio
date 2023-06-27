package com.finflio.feature_transactions.presentation.list_transactions.util

sealed class TransactionUiEvent {
    data class ShowSnackbar(val message: String) : TransactionUiEvent()
}