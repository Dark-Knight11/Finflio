package com.finflio.feature_transactions.domain.use_case

data class TransactionUseCases(
    val getTransactionUseCase: GetTransactionUseCase,
    val getTransactionsUseCase: GetTransactionsUseCase,
    val addTransactionUseCase: AddTransactionUseCase,
    val updateTransactionUseCase: UpdateTransactionUseCase,
    val deleteTransactionUseCase: DeleteTransactionUseCase,
    val getImagePathUseCase: GetImagePathUseCase,
    val deleteImageUseCase: DeleteImageUseCase,
    val getUnsettledTransactionsUseCase: GetUnsettledTransactionsUseCase,
    val getMonthTotalUseCase: GetMonthTotalUseCase
)
