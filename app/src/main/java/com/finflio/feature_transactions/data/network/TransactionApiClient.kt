package com.finflio.feature_transactions.data.network

import com.finflio.core.data.network.BaseApiClient
import com.finflio.core.data.util.UtilityMethods
import com.finflio.feature_transactions.data.models.remote.TransactionPostRequest
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TransactionApiClient @Inject constructor(
    private val apiService: TransactionApiService,
    utilityMethods: UtilityMethods
) : BaseApiClient(utilityMethods) {

    suspend fun getTransactions(page: Int, month: String) = getResult {
        apiService.getTransactions(page, month)
    }

    suspend fun getUnsettledTransactions(page: Int) = getResult {
        apiService.getUnsettledTransactions(page)
    }

    suspend fun createTransaction(transactionPostRequest: TransactionPostRequest, file: File?) =
        makePostRequest {
            apiService.createTransaction(
                amount = transactionPostRequest.amount.toString().toRequestBody(MultipartBody.FORM),
                category = transactionPostRequest.category.toRequestBody(MultipartBody.FORM),
                description = transactionPostRequest.description.toRequestBody(MultipartBody.FORM),
                paymentMethod = transactionPostRequest.paymentMethod
                    .toRequestBody(MultipartBody.FORM),
                timestamp = transactionPostRequest.timestamp.toString()
                    .toRequestBody(MultipartBody.FORM),
                type = transactionPostRequest.type.toRequestBody(MultipartBody.FORM),
                to = transactionPostRequest.to?.toRequestBody(MultipartBody.FORM),
                from = transactionPostRequest.from?.toRequestBody(MultipartBody.FORM),
                attachment = transactionPostRequest.attachment?.toRequestBody(MultipartBody.FORM),
                file = file?.let {
                    MultipartBody.Part.createFormData(
                        name = "image",
                        filename = file.name,
                        file.asRequestBody("image/jpeg".toMediaType())
                    )
                }
            )
        }

    suspend fun deleteTransaction(transactionId: String) = getResult {
        apiService.deleteTransaction(transactionId)
    }

    suspend fun updateTransaction(
        transactionId: String,
        transactionPostRequest: TransactionPostRequest,
        file: File?
    ) = makePostRequest {
        apiService.updateTransaction(
            transactionId,
            amount = transactionPostRequest.amount.toString().toRequestBody(MultipartBody.FORM),
            category = transactionPostRequest.category.toRequestBody(MultipartBody.FORM),
            description = transactionPostRequest.description.toRequestBody(MultipartBody.FORM),
            paymentMethod = transactionPostRequest.paymentMethod
                .toRequestBody(MultipartBody.FORM),
            timestamp = transactionPostRequest.timestamp.toString()
                .toRequestBody(MultipartBody.FORM),
            type = transactionPostRequest.type.toRequestBody(MultipartBody.FORM),
            to = transactionPostRequest.to?.toRequestBody(MultipartBody.FORM),
            from = transactionPostRequest.from?.toRequestBody(MultipartBody.FORM),
            attachment = transactionPostRequest.attachment?.toRequestBody(MultipartBody.FORM),
            file = file?.let {
                MultipartBody.Part.createFormData(
                    name = "image",
                    filename = file.name,
                    file.asRequestBody("image/jpeg".toMediaType())
                )
            }
        )
    }
}