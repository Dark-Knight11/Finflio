package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import com.patrykandpatrick.vico.core.extension.sumByFloat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.Month
import javax.inject.Inject

class GetMonthTotalUseCase @Inject constructor(
    private val repo: TransactionsRepository
) {
    suspend operator fun invoke(month: String): Flow<Float> {
        return channelFlow {
            repo.getTransactions().collectLatest { list ->
                send(
                    list.sumByFloat { transaction ->
                        if (transaction.type == "Expense" &&
                            transaction.timestamp.toLocalDate().month == Month.valueOf(month.uppercase())
                        ) {
                            transaction.amount
                        } else 0f
                    }
                )
            }
        }
    }
}