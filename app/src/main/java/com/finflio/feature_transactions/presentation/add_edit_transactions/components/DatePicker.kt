package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.Inter
import com.finflio.ui.theme.PrimaryText
import com.finflio.ui.theme.TransactionCardBg
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun DateTimePicker(
    dialogState: MaterialDialogState,
    initialDateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit = {}
) {
    val timeDialogState = rememberMaterialDialogState()
    var date by remember { mutableStateOf(LocalDate.MIN) }
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
                ),
                onClick = {
                    timeDialogState.show()
                }
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
        MaterialTheme(colors = MaterialTheme.colors.copy(onBackground = PrimaryText)) {
            datepicker(
                title = "SELECT ASSIGNMENT END DATE",
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = TransactionCardBg,
                    headerTextColor = GoldIcon,
                    calendarHeaderTextColor = PrimaryText,
                    dateActiveBackgroundColor = GoldIcon,
                    dateInactiveBackgroundColor = Color.Transparent,
                    dateActiveTextColor = Color.White,
                    dateInactiveTextColor = PrimaryText
                ),
                initialDate = initialDateTime.toLocalDate()
            ) {
                date = it
            }
        }
    }
    TimePicker(
        dialogState = timeDialogState,
        initialTime = initialDateTime.toLocalTime()
    ) { onDateTimeChange(LocalDateTime.of(date, it)) }
}