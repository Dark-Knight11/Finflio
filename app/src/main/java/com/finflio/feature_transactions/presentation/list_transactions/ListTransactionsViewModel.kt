package com.finflio.feature_transactions.presentation.list_transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.finflio.feature_transactions.domain.model.TransactionModel
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import com.finflio.feature_transactions.presentation.list_transactions.util.TransactionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    init {
        val currentMonth = LocalDateTime.now().month
        paginatedTransactions(currentMonth)
    }

    fun paginatedTransactions(month: Month) {
        viewModelScope.launch {
            useCases.getTransactionsUseCase(month).cachedIn(viewModelScope).collectLatest {
                _transactions.value = it
                getMonthTotal(month)
            }
        }
    }

    private suspend fun getMonthTotal(month: Month) {
        try {
            _monthTotal.value = useCases.getMonthTotalUseCase(month).toFloat()
        } catch (e: NullPointerException) {
            _monthTotal.value = 0f
            println(e.message)
        }
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.ChangeMonth -> {
                val month = Month.valueOf(event.month.uppercase())
                _month.value = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                paginatedTransactions(month)
            }
        }
    }
}
