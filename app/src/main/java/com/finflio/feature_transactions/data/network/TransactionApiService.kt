package com.finflio.feature_transactions.data.network

import com.finflio.feature_transactions.data.models.remote.TransactionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionApiService {

    @GET("transaction/all")
    suspend fun getTransactions(
        @Query("page") page: Int,
        @Query("month") month: String
    ): Response<TransactionResponse>
}