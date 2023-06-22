package com.finflio.feature_transactions.domain.mapper

import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.domain.model.Transaction
import java.time.LocalDateTime
import java.time.ZoneOffset

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        transactionId,
        userId,
        timestamp = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC),
        type = type,
        category = category,
        paymentMethod = paymentMethod,
        description = description,
        amount = amount,
        attachment = attachment,
        to = to,
        from = from
    )
}