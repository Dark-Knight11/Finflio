package com.finflio.feature_transactions.presentation.list_transactions.util

sealed class TransactionEvent {
    data class ChangeMonth(val month: String) : TransactionEvent()
}
