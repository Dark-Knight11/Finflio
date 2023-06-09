package com.finflio.core.data.mapper

import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.domain.model.Transaction
import java.time.ZoneOffset

fun Transaction.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = transactionId,
        timestamp = timestamp.toEpochSecond(ZoneOffset.UTC).times(1000),
        type = type,
        category = category,
        paymentMethod = paymentMethod,
        description = description,
        amount = amount,
        attachment = attachment,
        from = from,
        to = to,
        userId = userId
    )
}
