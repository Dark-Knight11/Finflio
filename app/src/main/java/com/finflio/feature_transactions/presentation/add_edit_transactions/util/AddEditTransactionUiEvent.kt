package com.finflio.feature_transactions.presentation.add_edit_transactions.util

sealed class AddEditTransactionUiEvent {
    object NavigateBack: AddEditTransactionUiEvent()
    data class ShowSnackBar(val message: String): AddEditTransactionUiEvent()
}
