package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.PaymentMethods
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.TransactionCardBg

@Composable
fun PaymentMethodDropdown(
    paymentMethod: PaymentMethods,
    onSelect: (PaymentMethods) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val customTextSelectionColors = TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = Color.Transparent
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .background(Color.White.copy(0.1f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Payment Method",
                color = Color.White,
                fontFamily = DMSans,
                fontWeight = FontWeight.Medium
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { }
            ) {
                CompositionLocalProvider(
                    LocalTextSelectionColors provides customTextSelectionColors
                ) {
                    CustomTextField(
                        value = paymentMethod.method,
                        readOnly = true,
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontFamily = DMSans,
                            fontWeight = FontWeight.Bold,
                            textIndent = TextIndent(7.sp),
                            textDecoration = TextDecoration.None
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Category Icon",
                                modifier = Modifier.clickable(
                                    remember { MutableInteractionSource() },
                                    null
                                ) {
                                    expanded = !expanded
                                }
                            )
                        },
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = paymentMethod.icon),
                                contentDescription = "payment method"
                            )
                        },
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            end = 16.dp,
                            top = 20.dp,
                            bottom = 10.dp
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = GoldIcon,
                            cursorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                            textColor = Color.White
                        ),
                        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        expanded = !expanded
                                    }
                                }
                            }
                        }
                    )
                }
                MaterialTheme(
                    colors = MaterialTheme.colors.copy(
                        background = TransactionCardBg,
                        surface = TransactionCardBg,
                        onBackground = TransactionCardBg
                    )
                ) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        Modifier.padding(top = 10.dp).exposedDropdownSize()
                    ) {
                        PaymentMethods.values().forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(remember { MutableInteractionSource() }, null) {
                                        onSelect(it)
                                        expanded = !expanded
                                    }
                                    .padding(vertical = 8.dp, horizontal = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = it.icon),
                                    contentDescription = "payment method",
                                    modifier = Modifier.width(70.dp)
                                )
                                Text(
                                    text = it.method,
                                    fontFamily = DMSans,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.clickable(
                                        remember { MutableInteractionSource() },
                                        null
                                    ) {
                                        onSelect(it)
                                        expanded = !expanded
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}
