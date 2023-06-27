package com.finflio.feature_transactions.data.network

import com.finflio.feature_transactions.data.models.remote.DeleteTransactionResponse
import com.finflio.feature_transactions.data.models.remote.TransactionPostResponse
import com.finflio.feature_transactions.data.models.remote.TransactionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    @Multipart
    suspend fun createTransaction(
        @Part("amount") amount: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("paymentMethod") paymentMethod: RequestBody?,
        @Part("timestamp") timestamp: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("to") to: RequestBody?,
        @Part("from") from: RequestBody?,
        @Part("attachment") attachment: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Response<TransactionPostResponse>

    @DELETE("transaction")
    suspend fun deleteTransaction(
        @Query("id") transactionId: String
    ): Response<DeleteTransactionResponse>

    @PUT("transaction")
    @Multipart
    suspend fun updateTransaction(
        @Query("id") transactionId: String,
        @Part("amount") amount: RequestBody?,
        @Part("category") category: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("paymentMethod") paymentMethod: RequestBody?,
        @Part("timestamp") timestamp: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("to") to: RequestBody?,
        @Part("from") from: RequestBody?,
        @Part("attachment") attachment: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Response<TransactionPostResponse>
}