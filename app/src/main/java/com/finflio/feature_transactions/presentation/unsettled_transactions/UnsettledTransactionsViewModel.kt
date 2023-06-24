package com.finflio.feature_transactions.presentation.unsettled_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class UnsettledTransactionsViewModel @Inject constructor(
    private val useCases: TransactionUseCases
) : ViewModel() {

    private val _unsettledTransactions =
        MutableStateFlow<PagingData<Transaction>>(PagingData.empty())
    val unsettledTransactions = _unsettledTransactions

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            useCases.getUnsettledTransactionsUseCase().cachedIn(viewModelScope).collectLatest {
                _unsettledTransactions.value = it
            }
        }
    }
}
