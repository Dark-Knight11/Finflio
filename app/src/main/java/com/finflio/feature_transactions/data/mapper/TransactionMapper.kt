package com.finflio.feature_transactions.data.mapper

import com.finflio.feature_transactions.data.data_source.TransactionEntity
import com.finflio.feature_transactions.domain.model.Transaction

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        transactionId = transactionId,
        timestamp = timestamp,
        type = type,
        category = category,
        paymentMethod = paymentMethod,
        description = description,
        amount = amount,
        attachment = attachment,
        from = from,
        to = to
    )
}

fun Transaction.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = transactionId,
        timestamp = timestamp,
        type = type,
        category = category,
        paymentMethod = paymentMethod,
        description = description,
        amount = amount,
        attachment = attachment,
        from = from,
        to = to
    )
}