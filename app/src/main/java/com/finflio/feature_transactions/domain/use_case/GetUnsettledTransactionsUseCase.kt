package com.finflio.feature_transactions.domain.use_case

import com.finflio.core.domain.model.Transaction
import com.finflio.core.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetUnsettledTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(): Flow<List<Transaction>> {
        return channelFlow {
            repository.getTransactions().collectLatest { transactionList ->
                send(transactionList.filter { it.type == "Unsettled" })
            }
        }
    }
}