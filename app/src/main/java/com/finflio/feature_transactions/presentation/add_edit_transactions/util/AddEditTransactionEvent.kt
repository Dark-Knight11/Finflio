package com.finflio.feature_transactions.presentation.add_edit_transactions.util

import java.time.LocalDateTime

sealed class AddEditTransactionEvent {
    object AddTransactionEvent: AddEditTransactionEvent()
    object EditTransactionEvent: AddEditTransactionEvent()
    object CancelTransaction: AddEditTransactionEvent()
    data class ChangeTimestamp(val timestamp: LocalDateTime): AddEditTransactionEvent()
    data class ChangeCategory(val categories: Categories): AddEditTransactionEvent()
    data class ChangeAmount(val amount: String):AddEditTransactionEvent()
    data class ChangeTo(val to: String): AddEditTransactionEvent()
    data class ChangeFrom(val from: String): AddEditTransactionEvent()
    data class ChangeDescription(val description: String): AddEditTransactionEvent()
    data class ChangePaymentMethod(val paymentMethods: PaymentMethods): AddEditTransactionEvent()
}
