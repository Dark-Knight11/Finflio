package com.finflio.feature_transactions.presentation.list_transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.finflio.feature_transactions.domain.model.TransactionModel
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.domain.util.InvalidTransactionException
import com.finflio.feature_transactions.presentation.list_transactions.util.TransactionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ListTransactionsViewModel @Inject constructor(
    private val useCases: TransactionUseCases
) : ViewModel() {

    val isRefreshing = mutableStateOf<Boolean>(true)

    private val _monthTotal = mutableStateOf(0f)
    val monthTotal: State<Float> = _monthTotal

    private val _month = mutableStateOf<String>(
        LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    )
    val month: State<String> = _month

    private val _transactions = MutableStateFlow<PagingData<TransactionModel>>(PagingData.empty())
    val transactions = _transactions
    fun paginatedTransactions(month: Month) {
        viewModelScope.launch {
            useCases.getTransactionsUseCase(month).cachedIn(viewModelScope).collectLatest {
                _monthTotal.value = useCases.getTransactionsUseCase.getTotal().toFloat()
                _transactions.value = it
            }
        }
    }

    init {
        paginatedTransactions(LocalDate.now().month)
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.ChangeMonth -> {
                viewModelScope.launch {
                    val month = Month.valueOf(event.month.uppercase())
                    _month.value = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    paginatedTransactions(Month.valueOf(event.month.uppercase()))
                }
            }

            is TransactionEvent.RestoreTransaction -> {
                viewModelScope.launch {
                    try {
                        useCases.addTransactionUseCase(event.transaction)
                    } catch (e: InvalidTransactionException) {
                        println(e.message)
                    }
                }
            }
        }
    }
}
