package com.finflio.feature_transactions.presentation.add_edit_transactions.util

import java.time.LocalDateTime

sealed class AddExpenseEvent {
    object AddTransactionEvent: AddExpenseEvent()
    object EditTransactionEvent: AddExpenseEvent()
    object CancelTransaction: AddExpenseEvent()
    data class ChangeTimestamp(val timestamp: LocalDateTime): AddExpenseEvent()
    data class ChangeCategory(val categories: Categories): AddExpenseEvent()
    data class ChangeAmount(val amount: String):AddExpenseEvent()
    data class ChangeTo(val to: String): AddExpenseEvent()
    data class ChangeDescription(val description: String): AddExpenseEvent()
    data class ChangePaymentMethod(val paymentMethods: PaymentMethods): AddExpenseEvent()
}
