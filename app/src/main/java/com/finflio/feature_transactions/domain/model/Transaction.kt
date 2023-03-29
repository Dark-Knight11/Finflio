package com.finflio.feature_transactions.domain.model

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime

data class Transaction(
    val transactionId: Int = 0,
    val timestamp: LocalDateTime,
    val type: String,
    val category: String,
    val paymentMethod: String,
    val description: String,
    val amount: Float,
    val attachment: String? = null,
    val from: String? = null,
    val to: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(LocalDateTime::class.java.classLoader) as LocalDateTime,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(transactionId)
        parcel.writeSerializable(timestamp)
        parcel.writeString(type)
        parcel.writeString(category)
        parcel.writeString(paymentMethod)
        parcel.writeString(description)
        parcel.writeFloat(amount)
        parcel.writeString(attachment)
        parcel.writeString(from)
        parcel.writeString(to)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}