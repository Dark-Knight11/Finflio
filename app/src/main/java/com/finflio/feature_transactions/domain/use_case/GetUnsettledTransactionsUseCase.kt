package com.finflio.feature_transactions.domain.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.finflio.feature_transactions.domain.mapper.toTransaction
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUnsettledTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    operator fun invoke(): Flow<PagingData<Transaction>> {
        return repository.getUnsettledTransaction().map {
            it.map { unsettledTransactionEntity ->
                unsettledTransactionEntity.toTransaction()
            }
        }
    }
}
