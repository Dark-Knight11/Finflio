package com.finflio.feature_transactions.data.mapper

import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity
import com.finflio.feature_transactions.data.models.remote.TransactionDTO

fun TransactionDTO.toUnsettledTransaction(): UnsettledTransactionEntity {
    return UnsettledTransactionEntity(
        transactionId = id,
        userId = userId,
        timestamp = timestamp,
        type = type,
        category = category,
        paymentMethod = paymentMethod,
        description = description,
        amount = amount,
        to = to,
        from = from,
        attachment = attachment
    )
}