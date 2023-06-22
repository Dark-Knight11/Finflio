package com.finflio.feature_transactions.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.finflio.feature_transactions.data.models.local.TransactionRemoteKeys

@Dao
interface TransactionRemoteKeysDao {

    @Query("SELECT * FROM transaction_remote_keys WHERE id =:id")
    suspend fun getRemoteKeys(id: String): TransactionRemoteKeys

    @Upsert
    suspend fun addAllRemoteKeys(remoteKeys: List<TransactionRemoteKeys>)

    @Query("DELETE FROM transaction_remote_keys")
    suspend fun deleteAllRemoteKeys()
}