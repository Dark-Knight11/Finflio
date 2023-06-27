package com.finflio.feature_transactions.domain.repository

import androidx.paging.PagingData
import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_transactions.data.models.local.MonthTotalEntity
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity
import com.finflio.feature_transactions.data.models.remote.DeleteTransactionResponse
import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
import com.finflio.feature_transactions.data.models.remote.TransactionPostResponse
import com.finflio.feature_transactions.domain.model.Transaction
import java.io.File
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {

    fun getTransactions(month: String): Flow<PagingData<Pair<TransactionEntity, Int>>>

    suspend fun getMonthTotal(month: String): MonthTotalEntity

    fun getUnsettledTransaction(): Flow<PagingData<UnsettledTransactionEntity>>

    suspend fun getTransaction(id: String): Transaction

    suspend fun getUnsettledTransaction(id: String): Transaction

    suspend fun deleteTransaction(transactionId: String): Flow<Resource<DeleteTransactionResponse>>

    suspend fun updateTransaction(
        transactionId: String,
        transactionPostRequest: TransactionPostRequest,
        file: File?
    ): Flow<Resource<TransactionPostResponse>>

    suspend fun addTransaction(transactionPostRequest: TransactionPostRequest, file: File?): Flow<Resource<TransactionPostResponse>>

    suspend fun deleteImage(imageID: String?)
}
