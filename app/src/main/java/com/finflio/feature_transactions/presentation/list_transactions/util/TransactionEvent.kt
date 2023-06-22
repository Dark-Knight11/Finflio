package com.finflio.feature_transactions.presentation.list_transactions.util

import com.finflio.feature_transactions.domain.model.Transaction

sealed class TransactionEvent {
    data class ChangeMonth(val month: String) : TransactionEvent()
    data class RestoreTransaction(val transaction: Transaction) : TransactionEvent()
}
