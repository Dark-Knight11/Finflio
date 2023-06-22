package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import java.time.Month
import javax.inject.Inject

class GetMonthTotalUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    suspend operator fun invoke(month: Month): Int {
        return repository.getMonthTotal(month.name).total
    }
}