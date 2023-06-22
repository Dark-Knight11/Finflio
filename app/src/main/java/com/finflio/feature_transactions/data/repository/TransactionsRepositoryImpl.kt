package com.finflio.feature_transactions.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cloudinary.Cloudinary
import com.finflio.BuildConfig
import com.finflio.core.data.data_source.FinflioDb
import com.finflio.core.data.mapper.toTransactionEntity
import com.finflio.core.data.repository.BaseRepo
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.network.TransactionApiClient
import com.finflio.feature_transactions.data.paging.TransactionRemoteMediator
import com.finflio.feature_transactions.domain.mapper.toTransaction
import com.finflio.feature_transactions.domain.model.Transaction
import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val apiClient: TransactionApiClient,
    private val finflioDb: FinflioDb
) : TransactionsRepository, BaseRepo() {

    val dao = finflioDb.transactionDao
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
            it.map {
                it to monthTotal
            }
        }
    }

    override suspend fun getTransaction(id: String): Transaction {
        return dao.getTransaction(id).toTransaction()
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction.toTransactionEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction.toTransactionEntity())
    }

    override suspend fun addTransaction(transaction: Transaction) {
        dao.addTransaction(transaction.toTransactionEntity())
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