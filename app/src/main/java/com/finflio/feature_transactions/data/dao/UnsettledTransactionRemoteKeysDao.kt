package com.finflio.feature_transactions.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.finflio.feature_transactions.data.models.local.UnsettledTransactionRemoteKeys

@Dao
interface UnsettledTransactionRemoteKeysDao {

    @Query("SELECT * FROM unsettled_transaction_remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id: String): UnsettledTransactionRemoteKeys

    @Upsert
    suspend fun addAllRemoteKeys(remoteKeys: List<UnsettledTransactionRemoteKeys>)

    @Query("DELETE FROM unsettled_transaction_remote_keys")
    suspend fun deleteAllRemoteKeys()
}