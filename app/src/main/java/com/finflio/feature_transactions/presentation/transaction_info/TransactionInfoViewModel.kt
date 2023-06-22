package com.finflio.feature_transactions.presentation.transaction_info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.presentation.transaction_info.util.TransactionInfoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionInfoViewModel @Inject constructor(
    private val useCase: TransactionUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _transaction = mutableStateOf<Transaction?>(null)
    val transaction: State<Transaction?> = _transaction

    init {
        viewModelScope.launch {
            val transactionId = savedStateHandle.get<String>("transactionId")
            val unsettled = savedStateHandle.get<Boolean>("unsettled")
            _transaction.value = transactionId?.let {
                useCase.getTransactionUseCase(it, unsettled ?: false)
            }
        }
    }

    fun onEvent(event: TransactionInfoEvent) {
        when (event) {
            is TransactionInfoEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    useCase.deleteTransactionUseCase(event.transaction ?: return@launch)
                    if (!event.transaction.attachment.isNullOrBlank()) {
                        useCase.deleteImageUseCase(event.transaction.attachment)
                    }
                }
            }
        }
    }
}
