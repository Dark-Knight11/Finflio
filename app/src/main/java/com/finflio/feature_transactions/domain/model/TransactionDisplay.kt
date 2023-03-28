package com.finflio.feature_transactions.domain.model

data class TransactionDisplay(
    val day: String,
    val transactions: List<Transaction>
)