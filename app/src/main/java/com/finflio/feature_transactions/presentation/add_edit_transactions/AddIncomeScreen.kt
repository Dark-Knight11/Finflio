package com.finflio.feature_transactions.presentation.add_edit_transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finflio.feature_transactions.presentation.add_edit_transactions.components.AddImageCard
import com.finflio.feature_transactions.presentation.add_edit_transactions.components.ImageItem
import com.finflio.feature_transactions.presentation.add_edit_transactions.components.InputCard
import com.finflio.feature_transactions.presentation.add_edit_transactions.components.SaveCancelButtons
import com.finflio.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination
@Composable
fun AddIncomeScreen(navigator: DestinationsNavigator) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground(
                colorStops = arrayOf(
                    0.0f to AddIncomeBG.copy(0.9f),
                    0.2f to TransactionCardBg
                ), angle = -70f, extraY = -120f
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .padding(top = 70.dp)
                .background(
                    brush = Brush.linearGradient(GreenGradient)
                )
                .padding(vertical = 13.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable(remember { MutableInteractionSource() }, null) {
                        navigator.popBackStack()
                    }
            )
            Text(
                text = "Income",
                fontFamily = DMSans,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        0.0f to AddIncomeBG.copy(0.5f),
                        1f to Color.Transparent,
                        radius = 1700f,
                        center = Offset(2000f, 1000f)
                    )
                )
                .verticalScroll(scrollState)
                .padding(top = 40.dp, start = 15.dp, end = 15.dp, bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InputCard(title = "Transaction") {
                Text(
                    text = "2:05 p.m.   |   Sep 01, 2022",
                    color = Color.White,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
            InputCard(title = "Category") {
                Text(
                    text = "Groceries",
                    color = Color.White,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
            InputCard(title = "Amount") {
                Text(
                    text = "₹25",
                    color = Color.White,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
            InputCard(title = "From") {
                Text(
                    text = "Anand",
                    color = Color.White,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
            InputCard(title = "Payment Method") {
                Text(
                    text = "Gpay",
                    color = Color.White,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
            InputCard(title = "Description") {
                Text(
                    text = "Weekly Groceries",
                    color = Color.White,
                    fontFamily = DMSans,
                    fontWeight = FontWeight.Bold
                )
            }
            var link by remember {
                mutableStateOf("https://i.imgur.com/eGUPkzW.jpeg")
            }
            if (link.isNotBlank()) {
                Spacer(modifier = Modifier.height(0.dp))
                ImageItem(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally),
                    link = link
                ) { link = "" }
            }
            AddImageCard() {
                link = "https://i.imgur.com/eGUPkzW.jpeg"
            }
            SaveCancelButtons(Modifier.fillMaxWidth())
        }
    }
}