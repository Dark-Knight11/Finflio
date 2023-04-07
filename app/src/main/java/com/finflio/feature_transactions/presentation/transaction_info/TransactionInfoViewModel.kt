package com.finflio.feature_transactions.presentation.transaction_info

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.core.domain.model.Transaction
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.presentation.transaction_info.util.TransactionInfoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionInfoViewModel @Inject constructor(
    private val useCase: TransactionUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _transaction = mutableStateOf<Transaction?>(null)
    val transaction: State<Transaction?> = _transaction

    init {
        viewModelScope.launch {
            savedStateHandle.get<Int>("transactionId")?.let {
                _transaction.value = useCase.getTransactionUseCase(it)
                println("Transaction Value: ${transaction.value}")
            }
        }
    }

    fun onEvent(event: TransactionInfoEvent) {
        when (event) {
            is TransactionInfoEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    useCase.deleteTransactionUseCase(event.transaction ?: return@launch)
                }
            }
        }
    }
}
