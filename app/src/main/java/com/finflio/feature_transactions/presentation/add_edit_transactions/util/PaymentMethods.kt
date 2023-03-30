package com.finflio.feature_transactions.presentation.add_edit_transactions.util

import com.finflio.R

enum class PaymentMethods(val icon: Int, val method: String) {
    GPay(R.drawable.google_pay, "GPay"),
    Cash(R.drawable.cash, "Cash"),
    PhonePe(R.drawable.ic_phonpe, "PhonePe"),
    Paytm(R.drawable.ic_paytm, "Paytm")
}