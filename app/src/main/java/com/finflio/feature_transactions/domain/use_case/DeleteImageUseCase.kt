package com.finflio.feature_transactions.domain.use_case

import com.finflio.feature_transactions.domain.repository.TransactionsRepository
import com.finflio.feature_transactions.domain.util.InvalidTransactionException
import com.finflio.feature_transactions.domain.util.invalidImage
import kotlin.jvm.Throws

class DeleteImageUseCase(
    private val repository: TransactionsRepository
) {
    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(imageUrl: String?) {
        invalidImage(imgUrl = imageUrl)
        val imageID = imageUrl?.let {
            val lastIndex = imageUrl.lastIndexOf("/")
            val imageNameWithExtension = if (lastIndex >= 0 && lastIndex < imageUrl.length - 1) {
                imageUrl.substring(lastIndex + 1)
            } else {
                null
            }
            imageNameWithExtension?.substringBeforeLast(".")
        }
        println("Image id: $imageID")
        repository.deleteImage(imageID)
    }
}
