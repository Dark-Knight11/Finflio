package com.finflio.feature_transactions.presentation.list_transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.feature_transactions.domain.model.TransactionDisplay
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.presentation.list_transactions.util.TransactionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ListTransactionViewModel @Inject constructor(
    private val useCases: TransactionUseCases
) : ViewModel() {
    private val _transactions = mutableStateOf<List<TransactionDisplay>>(emptyList())
    val transactions: State<List<TransactionDisplay>> = _transactions

    private val _monthTotal = mutableStateOf(0f)
    val monthTotal: State<Float> = _monthTotal

    init {
        viewModelScope.launch {
            async {
                useCases.getMonthTotalUseCase(
                    LocalDate.now().month.getDisplayName(
                        TextStyle.FULL,
                        Locale.ENGLISH
                    )
                ).collectLatest {
                    _monthTotal.value = it
                }
            }
            async {
                useCases.getTransactionsUseCase().collectLatest {
                    _transactions.value = it
                }
            }
        }
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.ChangeMonth -> {
                viewModelScope.launch {
                    useCases.getMonthTotalUseCase(event.month).collectLatest {
                        _monthTotal.value = it
                    }
                }
            }
        }
    }
}