package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.R
import com.finflio.ui.theme.Inter
import com.finflio.ui.theme.SecondaryText

@Composable
fun AddImageCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 25.dp)
            .clickable(remember { MutableInteractionSource() }, null) { onClick() }
            .graphicsLayer {
                shape = RoundedCornerShape(15.dp)
                clip = true
            }
            .background(Color.White.copy(0.08f))
            .padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_attach_file_24),
            contentDescription = "clip",
            tint = SecondaryText
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = "Add Attachment",
            color = SecondaryText,
            fontFamily = Inter,
            fontSize = 15.sp
        )
    }
}
