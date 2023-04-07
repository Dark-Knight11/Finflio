package com.finflio.feature_transactions.domain.util

import com.finflio.core.domain.model.Transaction

class InvalidTransactionException(message: String): Exception(message)

fun errors(transaction: Transaction) {
    if (transaction.category.isBlank())
        throw InvalidTransactionException("The category of the transaction can't be empty.")
    if (transaction.description.isBlank())
        throw InvalidTransactionException("The description of the transaction can't be empty.")
    if (transaction.paymentMethod.isBlank())
        throw InvalidTransactionException("The paymentMethod of the transaction can't be empty.")
    if (transaction.type.isBlank())
        throw InvalidTransactionException("The type of the transaction can't be empty.")
    if (transaction.description.isBlank())
        throw InvalidTransactionException("The description of the transaction can't be empty.")
    if (transaction.type == "Income" && transaction.to?.isNotBlank() == true)
        throw InvalidTransactionException("To field is applicable only for Expense.")
    if (transaction.type == "Expense" && transaction.from?.isNotBlank() == true)
        throw InvalidTransactionException("From field is applicable only for Income.")
    if (transaction.amount <= 0)
        throw InvalidTransactionException("Amount can't be negative or zero.")
}