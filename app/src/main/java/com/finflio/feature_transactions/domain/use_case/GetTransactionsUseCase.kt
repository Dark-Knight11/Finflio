package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.model.TransactionDisplay
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    operator fun invoke(): Flow<List<TransactionDisplay>> {
        return channelFlow {
            repo.getTransactions().collectLatest { transactionList ->
                launch(Dispatchers.IO) {
                    send(
                        listOf(
                            TransactionDisplay(
                                day = "Today",
                                transactions = transactionList.filter {
                                    it.timestamp.toLocalDate() == LocalDate.now()
                                }
                            ),
                            TransactionDisplay(
                                day = "Yesterday",
                                transactions = transactionList.filter {
                                    it.timestamp.toLocalDate() == LocalDate.now().minusDays(1)
                                }
                            ),
                        )
                    )
                }
            }
        }
    }
}

