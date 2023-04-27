package com.finflio.feature_transactions.presentation.add_edit_transactions.util

sealed class AddEditTransactionUiEvent {
    data class ShowSnackBar(val message: String): AddEditTransactionUiEvent()
    object NavigateBack: AddEditTransactionUiEvent()
    object ShowLoader: AddEditTransactionUiEvent()
    object HideLoader: AddEditTransactionUiEvent()
}
