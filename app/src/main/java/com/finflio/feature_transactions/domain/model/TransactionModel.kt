package com.finflio.feature_transactions.domain.model

sealed class TransactionModel {
    data class TransactionItem(val transaction: Transaction) : TransactionModel()
    data class SeparatorItem(val separator: String) : TransactionModel()
}