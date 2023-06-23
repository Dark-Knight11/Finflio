package com.finflio.feature_transactions.domain.util

import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
class InvalidTransactionException(message: String) : Exception(message)

fun errors(transactionPostRequest: TransactionPostRequest) {
    if (transactionPostRequest.category.isBlank()) {
        throw InvalidTransactionException(
            "The category of the transactionPostRequest can't be empty."
        )
    } else if (transactionPostRequest.description.isBlank()) {
        throw InvalidTransactionException(
            "The description of the transactionPostRequest can't be empty."
        )
    } else if (transactionPostRequest.paymentMethod.isBlank()) {
        throw InvalidTransactionException(
            "The paymentMethod of the transactionPostRequest can't be empty."
        )
    } else if (transactionPostRequest.type.isBlank()) {
        throw InvalidTransactionException("The type of the transactionPostRequest can't be empty.")
    } else if (transactionPostRequest.description.isBlank()) {
        throw InvalidTransactionException(
            "The description of the transactionPostRequest can't be empty."
        )
    } else if (transactionPostRequest.type == "Income" && transactionPostRequest.to?.isNotBlank() == true) {
        throw InvalidTransactionException("To field is applicable only for Expense.")
    } else if (transactionPostRequest.type == "Expense" && transactionPostRequest.from?.isNotBlank() == true) {
        throw InvalidTransactionException("From field is applicable only for Income.")
    } else if (transactionPostRequest.type == "Unsettled" && transactionPostRequest.to.isNullOrBlank() && transactionPostRequest.from.isNullOrBlank()) {
        throw InvalidTransactionException(
            "One of the field should be filled between \"To\" and \"From\""
        )
    } else if (transactionPostRequest.type == "Unsettled" && transactionPostRequest.to?.isNotBlank() == true && transactionPostRequest.from?.isNotBlank() == true) {
        throw InvalidTransactionException(
            "Only one field is applicable between \"To\" and \"From\""
        )
    } else if (transactionPostRequest.amount <= 0) {
        throw InvalidTransactionException("Amount can't be negative or zero.")
    } else {
        invalidImage(transactionPostRequest.attachment)
    }
}

fun invalidImage(imgUrl: String?) {
    if (imgUrl?.contains("cloudinary") == false) {
        throw InvalidTransactionException("Local Image Path not allowed")
    }
}
