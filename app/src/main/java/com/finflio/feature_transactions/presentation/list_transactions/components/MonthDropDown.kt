package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.core.presentation.util.toDp
import com.finflio.feature_transactions.presentation.list_transactions.util.shortToLongMonth
import com.finflio.ui.theme.Gold
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.OrangeRed
import com.finflio.ui.theme.TransactionCardBg
import com.finflio.ui.theme.screenSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.Locale

@Composable
fun MonthDropDown(
    modifier: Modifier = Modifier,
    month: String,
    currentYear: Int,
    onSelect: (String) -> Unit
) {
    val months = DateFormatSymbols.getInstance(Locale.getDefault()).shortMonths.toList()
    var year by remember {
        mutableStateOf(currentYear)
    }
    var selectedText by remember { mutableStateOf(month) }
    var mExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var dropdownWidth by remember { mutableStateOf(0.dp) }
    val screenWidth = screenSize.width
    var monthSelectorPositionX by remember { mutableStateOf(0.dp) }
    val calculatedOffset =
        remember { derivedStateOf { -monthSelectorPositionX + (screenWidth - dropdownWidth) / 2 } }

    ExposedDropdownMenuBox(
        expanded = mExpanded,
        onExpandedChange = { }
    ) {
        Row(
            modifier = Modifier
                .onGloballyPositioned {
                    monthSelectorPositionX = it.positionInWindow().x.toDp
                }
                .padding(bottom = 10.dp)
                .clickable(remember { MutableInteractionSource() }, null) {
                    mExpanded = !mExpanded
                }
                .border(
                    BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(100)
                )
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.ArrowDropDown,
                "contentDescription",
                Modifier.clickable(remember { MutableInteractionSource() }, null) {
                    mExpanded = !mExpanded
                },
                tint = OrangeRed
            )
            Text(text = selectedText, color = Color.White)
        }

        MaterialTheme(
            MaterialTheme.colors.copy(
                background = TransactionCardBg,
                surface = TransactionCardBg,
                onBackground = TransactionCardBg
            )
        ) {
            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = modifier
                    .exposedDropdownSize(false)
                    .onSizeChanged { dropdownWidth = it.width.toDp },
                offset = DpOffset(calculatedOffset.value, 0.dp)
            ) {
                months.windowed(3, 3, true).forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.forEach { month ->
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        shape = RoundedCornerShape(8.dp)
                                        clip = true
                                    }
                                    .then(
                                        if (month == selectedText.slice(IntRange(0, 2))) {
                                            Modifier
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        0.0f to Gold,
                                                        1.0f to Gold.copy(0.5f)
                                                    ),
                                                    alpha = 0.5f
                                                )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .padding(vertical = 5.dp, horizontal = 20.dp)
                            ) {
                                Text(
                                    text = month,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    modifier =
                                    if (month != selectedText.slice(IntRange(0, 2))) {
                                        Modifier.clickable(
                                            remember { MutableInteractionSource() },
                                            null
                                        ) {
                                            selectedText = shortToLongMonth(month)
                                            onSelect(selectedText)
                                            scope.launch {
                                                delay(300)
                                                mExpanded = !mExpanded
                                            }
                                        }
                                    } else {
                                        Modifier
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthPicker(
    month: String,
    year: Int,
    confirmButtonCLicked: (String, Int) -> Unit
) {
    val months = DateFormatSymbols.getInstance(Locale.getDefault()).shortMonths.toList()
    var displayMonth = month
    var displayYear = year
    var slice = if (month == "September") {
        month.slice(0..3)
    } else {
        month.slice(0..2)
    }

    var selectedMonth by remember {
        mutableStateOf(months[months.indexOf(slice)])
    }
    var selectedYear by remember {
        mutableIntStateOf(year)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    var visible by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .clickable(remember { MutableInteractionSource() }, null) {
                visible = !visible
            }
            .padding(bottom = 10.dp)
            .border(
                BorderStroke(
                    1.dp,
                    brush = Brush.linearGradient(
                        0.0f to Color.White,
                        1.0f to Gold
                    )
                ),
                shape = RoundedCornerShape(100)
            )
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = displayMonth, color = Color.White)
        if (displayYear != LocalDate.now().year) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = displayYear.toString(), color = Color.White)
        }
    }
    if (visible) {
        AlertDialog(
            backgroundColor = TransactionCardBg,
            shape = RoundedCornerShape(10),
            title = {
                "Select Month and Year"
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .rotate(90f)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        selectedYear--
                                    }
                                ),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Gold.copy(0.7f)

                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 15.dp),
                            text = selectedYear.toString(),
                            fontSize = 23.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .rotate(-90f)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        selectedYear++
                                    }
                                ),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Gold.copy(0.7f)
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        elevation = 0.dp,
                        backgroundColor = TransactionCardBg
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterVertically
                            ),
                            maxItemsInEachRow = 3
                        ) {
                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                selectedMonth = it
                                            }
                                        )
                                        .background(
                                            color = Color.Transparent
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val animatedSize by animateDpAsState(
                                        targetValue = if (selectedMonth == it) 60.dp else 0.dp,
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing
                                        ),
                                        label = "selected state"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(animatedSize)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    0.0f to Gold,
                                                    1.0f to Gold.copy(0.5f)
                                                ),
                                                alpha = 0.7f,
                                                shape = CircleShape
                                            )
                                    )
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, bottom = 30.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = {
                            selectedMonth = months[months.indexOf(month.slice(0..2))]
                            selectedYear = year
                            visible = false
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }

                    OutlinedButton(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = {
                            val longMonth = shortToLongMonth(selectedMonth)
                            displayMonth = longMonth
                            displayYear = selectedYear
                            confirmButtonCLicked(
                                longMonth,
                                selectedYear
                            )
                            visible = false
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = GoldIcon),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "OK",
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            },
            onDismissRequest = {
                selectedMonth = months[months.indexOf(month.slice(0..2))]
                selectedYear = year
                visible = false
            }
        )
    }
}
