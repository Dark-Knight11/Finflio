package com.finflio.feature_transactions.presentation.transaction_info.util

import com.finflio.feature_transactions.domain.model.Transaction

sealed class TransactionInfoEvent {
    data class DeleteTransaction(val transaction: Transaction?) : TransactionInfoEvent()
}
