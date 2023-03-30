package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.Inter
import com.finflio.ui.theme.SecondaryText
import com.finflio.ui.theme.TransactionCardBg
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun TimePicker(
    dialogState: MaterialDialogState,
    initialTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit = {}
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(
                text = "OK",
                textStyle = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = GoldIcon
                )
            )
            negativeButton(
                text = "CANCEL",
                textStyle = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = GoldIcon
                )
            )
        },
        backgroundColor = TransactionCardBg,
        shape = RoundedCornerShape(20.dp)
    ) {
        timepicker(
            title = "SELECT Transaction TIME",
            colors = TimePickerDefaults.colors(
                selectorColor = GoldIcon,
                activeBackgroundColor = GoldIcon,
                activeTextColor = Color.White,
                inactiveTextColor = SecondaryText,
                inactiveBackgroundColor = SecondaryText.copy(alpha = 0.3f),
                headerTextColor = GoldIcon,
                selectorTextColor = Color.White
            ),
            initialTime = initialTime
        ) {
            onTimeChange(it)
        }
    }
}
