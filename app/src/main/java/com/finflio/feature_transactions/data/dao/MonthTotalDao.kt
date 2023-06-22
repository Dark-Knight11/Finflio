package com.finflio.feature_transactions.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finflio.feature_transactions.data.models.local.MonthTotalEntity

@Dao
interface MonthTotalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(monthTotalEntity: MonthTotalEntity)

    @Query("Select * from month_total where month = :month")
    suspend fun getMonthTotal(month: String): MonthTotalEntity
}
