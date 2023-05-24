package com.finflio.feature_transactions.domain.model

import com.finflio.core.domain.model.Transaction

data class TransactionDisplay(
    val day: String,
    val transactions: List<Transaction>
)
