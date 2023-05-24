package com.finflio.feature_transactions.presentation.unsettled_transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finflio.core.domain.model.Transaction
import com.finflio.feature_transactions.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class UnsettledTransactionsViewModel @Inject constructor(
    private val useCases: TransactionUseCases
) : ViewModel() {
    private val _unsettledTransactions = mutableStateOf<List<Transaction>>(emptyList())
    val unsettledTransactions: State<List<Transaction>> = _unsettledTransactions

    init {
        viewModelScope.launch {
            useCases.getUnsettledTransactionsUseCase().collectLatest {
                _unsettledTransactions.value = it
            }
        }
    }
}
