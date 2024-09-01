package com.finflio.feature_transactions.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finflio.feature_transactions.data.models.local.MonthTotalEntity
import java.time.Year

@Dao
interface MonthTotalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(monthTotalEntity: MonthTotalEntity)

    @Query("Select * from month_total where month = :month AND year = :year")
    suspend fun getMonthTotal(month: String, year: Int): MonthTotalEntity
}
