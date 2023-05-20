package com.finflio.feature_transactions.presentation.add_edit_transactions.util

import androidx.compose.ui.graphics.Color
import com.finflio.R

enum class Categories(val icon: Int, val category: String, val colors: List<Color>) {
    Food(R.drawable.fast_food, "Food", listOf(Color(0xFFFF7F09), Color(0xFFE33C3C))),
    Transportation(R.drawable.ic_transportation, "Transportation", listOf(Color(0xFFE33C3C), Color(0xFF950D0D))),
    Smoothie(R.drawable.drink, "Smoothie", listOf(Color(0xFF98C900), Color(0xFFFABB05))),
    Bill(R.drawable.ic_bill, "Bill", listOf(Color(0xFFF7FC13), Color(0xFF12FDEF))),
    Life(R.drawable.ic_life, "Life", listOf(Color(0xFF8EFDAD), Color(0xFF27CC55))),
    Groceries(R.drawable.ic_cart, "Groceries", listOf(Color(0xFF61D8D8), Color(0xFF3972D4))),
    Electronics(R.drawable.ic_electronics, "Electronics", listOf(Color(0xFFE3B53C), Color(0xFFE3823C))),
    Investments(R.drawable.ic_invetsment, "Investments", listOf(Color(0xFFFFE870), Color(0xFFE3B53C))),
    Apparels(R.drawable.ic_clothes, "Apparels", listOf(Color(0xFFA858EE), Color(0xFF6C40D9))),
}