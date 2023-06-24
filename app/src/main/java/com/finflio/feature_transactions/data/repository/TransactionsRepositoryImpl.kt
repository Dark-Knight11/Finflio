package com.finflio.feature_transactions.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cloudinary.Cloudinary
import com.finflio.BuildConfig
import com.finflio.core.data.data_source.FinflioDb
import com.finflio.core.data.repository.BaseRepo
import com.finflio.feature_transactions.data.models.local.MonthTotalEntity
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity
import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
import com.finflio.feature_transactions.data.network.TransactionApiClient
import com.finflio.feature_transactions.data.paging.TransactionRemoteMediator
import com.finflio.feature_transactions.data.paging.UnsettledTransactionsRemoteMediator
import com.finflio.feature_transactions.domain.mapper.toTransaction
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TransactionsRepositoryImpl @Inject constructor(
    private val apiClient: TransactionApiClient,
    private val finflioDb: FinflioDb
) : TransactionsRepository, BaseRepo() {

    private val dao = finflioDb.transactionDao
    private val unsettledDao = finflioDb.unsettledTransactionDao
    private val monthTotalDao = finflioDb.monthTotalDao
    override fun getTransactions(month: String): Flow<PagingData<Pair<TransactionEntity, Int>>> {
        val pagingSourceFactory = { dao.getTransactions() }
        val remoteMediator = TransactionRemoteMediator(
            apiClient = apiClient,
            finflioDb = finflioDb,
            month = month
        )
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow.map {
            val monthTotal = remoteMediator.monthTotal
            it.map { transactionEntity ->
                transactionEntity to monthTotal
            }
        }
    }

    override suspend fun getMonthTotal(month: String): MonthTotalEntity {
        return monthTotalDao.getMonthTotal(month)
    }

    override fun getUnsettledTransaction(): Flow<PagingData<UnsettledTransactionEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = UnsettledTransactionsRemoteMediator(
                apiClient = apiClient,
                finflioDb = finflioDb
            ),
            pagingSourceFactory = { unsettledDao.getUnsettledTransactions() }
        ).flow
    }

    override suspend fun getTransaction(id: String): Transaction {
        return dao.getTransaction(id).toTransaction()
    }

    override suspend fun getUnsettledTransaction(id: String): Transaction {
        return unsettledDao.getUnsettledTransaction(id).toTransaction()
    }

    override suspend fun deleteTransaction(transactionId: String) = makeRequest {
        apiClient.deleteTransaction(transactionId)
    }

    override suspend fun updateTransaction(
        transactionId: String,
        transactionPostRequest: TransactionPostRequest
    ) = makeRequest {
        apiClient.updateTransaction(transactionId, transactionPostRequest)
    }

    override suspend fun addTransaction(transactionPostRequest: TransactionPostRequest) =
        makeRequest {
            apiClient.createTransaction(transactionPostRequest)
        }

    override suspend fun deleteImage(imageID: String?) {
        val cloudinary = Cloudinary(BuildConfig.CLOUDINARY_URL)
        val deleteParams = mapOf("invalidate" to true)
        withContext(Dispatchers.IO) {
            val result = cloudinary.uploader().destroy("finflio/$imageID", deleteParams)
            println("Result: ${result.values}")
        }
    }
}