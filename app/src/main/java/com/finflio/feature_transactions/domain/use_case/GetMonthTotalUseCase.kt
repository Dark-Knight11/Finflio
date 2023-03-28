package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.repository.TransactionsRepository
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
                var total = 0f
                list.filter {
                    it.timestamp.toLocalDate().month == Month.valueOf(month.uppercase())
                }.forEach {
                    if (it.type == "Income")
                        total += it.amount
                    else total -= it.amount
                }
                println("Total: $total")
                send(total)
            }
        }
    }
}