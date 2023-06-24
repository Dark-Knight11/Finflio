package com.finflio.feature_transactions.data.paging

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.finflio.core.data.data_source.FinflioDb
import com.finflio.core.data.network.resource.Resource
import com.finflio.feature_transactions.data.mapper.toUnsettledTransaction
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionEntity
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionRemoteKeys
import com.finflio.feature_transactions.data.network.TransactionApiClient
import okio.IOException
import retrofit2.HttpException

class UnsettledTransactionsRemoteMediator(
    private val apiClient: TransactionApiClient,
    private val finflioDb: FinflioDb
) : RemoteMediator<Int, UnsettledTransactionEntity>() {

    private val unsettledTransactionDao = finflioDb.unsettledTransactionDao
    private val unsettledTransactionRemoteKeysDao = finflioDb.unsettledTransactionRemoteKeysDao
    var shouldUpdate = true

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsettledTransactionEntity>
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
                val response = apiClient.getUnsettledTransactions(currentPage)
                when (response.status) {
                    Resource.Status.SUCCESS -> {
                        if (response.data != null) {
                            val endOfPaginationReached = currentPage + 1 > response.data.totalPages
                            val prevPage = if (currentPage == 1) null else currentPage - 1
                            val nextPage = if (endOfPaginationReached) null else currentPage + 1
//                            shouldUpdate = !endOfPaginationReached

                            finflioDb.withTransaction {
                                if (loadType == LoadType.REFRESH) {
                                    unsettledTransactionDao.deleteAllUnsettledTransaction()
                                    unsettledTransactionRemoteKeysDao.deleteAllRemoteKeys()
                                }
                                val keys = response.data.transactions.map { transactionDTO ->
                                    UnsettledTransactionRemoteKeys(
                                        id = transactionDTO.id,
                                        prevPage = prevPage,
                                        nextPage = nextPage
                                    )
                                }
                                unsettledTransactionRemoteKeysDao.addAllRemoteKeys(
                                    remoteKeys = keys
                                )
                                val transactionEntities =
                                    response.data.transactions.map { it.toUnsettledTransaction() }
                                unsettledTransactionDao.upsertUnsettledTransaction(
                                    transactionEntities
                                )
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
                        println(response.message)
                        if (response.message == "No Transactions") {
                            finflioDb.withTransaction {
                                unsettledTransactionDao.deleteAllUnsettledTransaction()
                                unsettledTransactionRemoteKeysDao.deleteAllRemoteKeys()
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
        state: PagingState<Int, UnsettledTransactionEntity>
    ): UnsettledTransactionRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.transactionId?.let { id ->
                unsettledTransactionRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UnsettledTransactionEntity>
    ): UnsettledTransactionRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { transactionEntity ->
                unsettledTransactionRemoteKeysDao.getRemoteKeys(
                    id = transactionEntity.transactionId
                )
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UnsettledTransactionEntity>
    ): UnsettledTransactionRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { transactionEntity ->
                unsettledTransactionRemoteKeysDao.getRemoteKeys(
                    id = transactionEntity.transactionId
                )
            }
    }
}