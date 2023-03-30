package com.finflio.feature_transactions.presentation.add_edit_transactions.util

sealed class AddExpenseUiEvent {
    object NavigateBack: AddExpenseUiEvent()
    data class ShowSnackBar(val message: String): AddExpenseUiEvent()
}
