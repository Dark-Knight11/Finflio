package com.finflio.feature_transactions.presentation.list_transactions.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.finflio.core.presentation.util.toDp
import com.finflio.feature_transactions.presentation.list_transactions.util.shortToLongMonth
import com.finflio.ui.theme.Gold
import com.finflio.ui.theme.OrangeRed
import com.finflio.ui.theme.screenSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MonthDropDown(
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit
) {
    val months = DateFormatSymbols.getInstance(Locale.ENGLISH).shortMonths.toList()
    val dateFormat: DateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    var selectedText by remember { mutableStateOf(dateFormat.format(Date())) }
    var mExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var dropdownWidth by remember {mutableStateOf(0.dp)}
    val screenWidth = screenSize.width
    var monthSelectorPositionX by remember {mutableStateOf(0.dp)}
    val calculatedOffset = remember {derivedStateOf {-monthSelectorPositionX + (screenWidth - dropdownWidth)/2}}

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
                Icons.Default.ArrowDropDown, "contentDescription",
                Modifier.clickable(remember { MutableInteractionSource() }, null) {
                    mExpanded = !mExpanded
                },
                tint = OrangeRed
            )
            Text(text = selectedText, color = Color.White)
        }

        MaterialTheme(MaterialTheme.colors.copy(surface = Color(0xFF141326))) {
            DropdownMenu(
                expanded = mExpanded,
                onDismissRequest = { mExpanded = false },
                modifier = modifier.exposedDropdownSize(false)
                    .onSizeChanged { dropdownWidth = it.width.toDp },
                offset = DpOffset(calculatedOffset.value, 0.dp),
            ) {
                months.windowed(3, 3, true).forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.forEach { month ->
                            if (month == selectedText.slice(IntRange(0, 2)))
                                Box(
                                    modifier = Modifier
                                        .graphicsLayer {
                                            shape = RoundedCornerShape(8.dp)
                                            clip = true
                                        }
                                        .background(
                                            brush = Brush.linearGradient(
                                                0.0f to Gold,
                                                1.0f to Gold.copy(0.5f)
                                            ),
                                            alpha = 0.5f
                                        )
                                        .padding(vertical = 5.dp, horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = month,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier,
                                        color = Color.White
                                    )
                                }
                            else {
                                Box(
                                    modifier = Modifier
                                        .graphicsLayer {
                                            shape = RoundedCornerShape(8.dp)
                                            clip = true
                                        }
                                        .padding(vertical = 5.dp, horizontal = 20.dp)
                                ) {
                                    Text(
                                        text = month,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.clickable(
                                            remember { MutableInteractionSource() }, null
                                        ) {
                                            selectedText = shortToLongMonth(month)
                                            onSelect(selectedText)
                                            scope.launch {
                                                delay(300)
                                                mExpanded = !mExpanded
                                            }
                                        },
                                        color = Color.White,
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}