package com.finflio.feature_transactions.domain.util

import com.finflio.core.domain.model.Transaction

class InvalidTransactionException(message: String) : Exception(message)

fun errors(transaction: Transaction) {
    if (transaction.category.isBlank())
        throw InvalidTransactionException("The category of the transaction can't be empty.")
    else if (transaction.description.isBlank())
        throw InvalidTransactionException("The description of the transaction can't be empty.")
    else if (transaction.paymentMethod.isBlank())
        throw InvalidTransactionException("The paymentMethod of the transaction can't be empty.")
    else if (transaction.type.isBlank())
        throw InvalidTransactionException("The type of the transaction can't be empty.")
    else if (transaction.description.isBlank())
        throw InvalidTransactionException("The description of the transaction can't be empty.")
    else if (transaction.type == "Income" && transaction.to?.isNotBlank() == true)
        throw InvalidTransactionException("To field is applicable only for Expense.")
    else if (transaction.type == "Expense" && transaction.from?.isNotBlank() == true)
        throw InvalidTransactionException("From field is applicable only for Income.")
    else if (transaction.type == "Unsettled" && transaction.to.isNullOrBlank() && transaction.from.isNullOrBlank())
        throw InvalidTransactionException("One of the field should be filled between \"To\" and \"From\"")
    else if (transaction.type == "Unsettled" && transaction.to?.isNotBlank() == true && transaction.from?.isNotBlank() == true)
        throw InvalidTransactionException("Only one field is applicable between \"To\" and \"From\"")
    else if (transaction.amount <= 0)
        throw InvalidTransactionException("Amount can't be negative or zero.")
    else invalidImage(transaction.attachment)
}

fun invalidImage(imgUrl: String?) {
    if (imgUrl?.contains("cloudinary") == false)
        throw InvalidTransactionException("Local Image Path not allowed")
}