package com.finflio.feature_transactions.data.network

import com.finflio.feature_transactions.data.models.remote.TransactionPostResponse
import com.finflio.feature_transactions.data.models.remote.TransactionResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionApiService {

    @GET("transaction/all")
    suspend fun getTransactions(
        @Query("page") page: Int,
        @Query("month") month: String
    ): Response<TransactionResponse>

    @GET("transaction/unsettled")
    suspend fun getUnsettledTransactions(
        @Query("page") page: Int
    ): Response<TransactionResponse>

    @POST("transaction")
    suspend fun createTransaction(
        @Body transaction: JsonElement
    ): Response<TransactionPostResponse>
}