package com.finflio.feature_transactions.presentation.add_edit_transactions.util

import android.content.Context
import java.time.LocalDateTime

sealed class AddEditTransactionEvent {
    object CancelTransaction: AddEditTransactionEvent()
    data class AddTransactionEvent(val context: Context): AddEditTransactionEvent()
    data class EditTransactionEvent(val context: Context): AddEditTransactionEvent()
    data class ChangeTimestamp(val timestamp: LocalDateTime): AddEditTransactionEvent()
    data class ChangeCategory(val category: Categories): AddEditTransactionEvent()
    data class ChangeAmount(val amount: String):AddEditTransactionEvent()
    data class ChangeTo(val to: String): AddEditTransactionEvent()
    data class ChangeFrom(val from: String): AddEditTransactionEvent()
    data class ChangeDescription(val description: String): AddEditTransactionEvent()
    data class ChangePaymentMethod(val paymentMethods: PaymentMethods): AddEditTransactionEvent()
    data class ChangeImagePath(val imagePath: String?): AddEditTransactionEvent()
}
