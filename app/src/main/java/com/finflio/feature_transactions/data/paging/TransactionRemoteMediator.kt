package com.finflio.feature_transactions.data.paging

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.finflio.core.data.data_source.FinflioDb
import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_transactions.data.mapper.toTransactionEntity
import com.finflio.feature_transactions.data.models.local.MonthTotalEntity
import com.finflio.feature_transactions.data.models.local.TransactionEntity
import com.finflio.feature_transactions.data.models.local.TransactionRemoteKeys
import com.finflio.feature_transactions.data.network.TransactionApiClient
import java.time.LocalDate
import okio.IOException
import retrofit2.HttpException

class TransactionRemoteMediator(
    private val apiClient: TransactionApiClient,
    private val finflioDb: FinflioDb,
    private val month: String,
    private val year: Int?
) : RemoteMediator<Int, TransactionEntity>() {

    private val transactionDao = finflioDb.transactionDao
    private val transactionRemoteKeysDao = finflioDb.transactionRemoteKeysDao
    private val monthTotalDao = finflioDb.monthTotalDao
    var monthTotal = 0
    var shouldUpdate = true

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TransactionEntity>
    ): MediatorResult {
        return if (shouldUpdate) {
            try {
                val currentPage = when (loadType) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextPage?.minus(1) ?: 1
                    }

                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                        val prevPage = remoteKeys?.prevPage
                            ?: return MediatorResult.Success(
                                endOfPaginationReached = remoteKeys != null
                            )
                        prevPage
                    }

                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        val nextPage = remoteKeys?.nextPage
                            ?: return MediatorResult.Success(
                                endOfPaginationReached = remoteKeys != null
                            )
                        nextPage
                    }
                }
                val response = apiClient.getTransactions(currentPage, month, year)
                when (response.status) {
                    Resource.Status.SUCCESS -> {
                        if (response.data != null) {
                            val endOfPaginationReached = currentPage + 1 > response.data.totalPages
                            val prevPage = if (currentPage == 1) null else currentPage - 1
                            val nextPage = if (endOfPaginationReached) null else currentPage + 1
                            monthTotal = response.data.monthTotal ?: 0
//                            shouldUpdate = !endOfPaginationReached

                            finflioDb.withTransaction {
                                if (loadType == LoadType.REFRESH) {
                                    transactionDao.deleteAllTransactions()
                                    transactionRemoteKeysDao.deleteAllRemoteKeys()
                                    monthTotalDao.addData(
                                        MonthTotalEntity(
                                            month,
                                            monthTotal,
                                            year ?: LocalDate.now().year
                                        )
                                    )
                                }
                                val keys = response.data.transactions.map { transactionDTO ->
                                    TransactionRemoteKeys(
                                        id = transactionDTO.id,
                                        prevPage = prevPage,
                                        nextPage = nextPage
                                    )
                                }
                                transactionRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                                val transactionEntities =
                                    response.data.transactions.map { it.toTransactionEntity() }
                                transactionDao.upsertTransactions(transactionEntities)
                            }
                            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                        } else {
                            println("success error")
                            MediatorResult.Error(
                                Error("Something Went wrong")
                            )
                        }
                    }

                    Resource.Status.ERROR -> {
                        if (response.message == "No Transactions") {
                            finflioDb.withTransaction {
                                transactionDao.deleteAllTransactions()
                                transactionRemoteKeysDao.deleteAllRemoteKeys()
                            }
                        }
                        MediatorResult.Error(
                            Error(response.message)
                        )
                    }

                    else -> {
                        MediatorResult.Error(
                            Error("Something Went wrong")
                        )
                    }
                }
            } catch (e: IOException) {
                MediatorResult.Error(e)
            } catch (e: HttpException) {
                MediatorResult.Error(e)
            }
        } else {
            MediatorResult.Error(Error("End of pagination"))
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, TransactionEntity>
    ): TransactionRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.transactionId?.let { id ->
                transactionRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, TransactionEntity>
    ): TransactionRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { transactionEntity ->
                transactionRemoteKeysDao.getRemoteKeys(id = transactionEntity.transactionId)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, TransactionEntity>
    ): TransactionRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { transactionEntity ->
                transactionRemoteKeysDao.getRemoteKeys(id = transactionEntity.transactionId)
            }
    }
}