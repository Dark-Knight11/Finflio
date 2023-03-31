package com.finflio.feature_transactions.presentation.add_edit_transactions.components

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
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.feature_transactions.presentation.add_edit_transactions.util.Categories
import com.finflio.feature_transactions.presentation.transaction_info.components.CategoryFab
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.TransactionCardBg

@Composable
fun CategoryDropDown(
    category: Categories,
    onSelect: (Categories) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = Transparent,
        backgroundColor = Transparent,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .background(White.copy(0.1f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Category",
                color = White,
                fontFamily = DMSans,
                fontWeight = FontWeight.Medium
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { }
            ) {
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    CustomTextField(
                        value = category.category,
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
                            CategoryFab(
                                icon = category.icon,
                                modifier = Modifier.padding(bottom = 10.dp),
                                size = 35.dp,
                                colors = category.colors
                            )
                        },
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            end = 16.dp,
                            top = 20.dp,
                            bottom = 30.dp
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = GoldIcon,
                            cursorColor = Transparent,
                            backgroundColor = Transparent,
                            textColor = White,
                        ),
                        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        expanded = !expanded
                                    }
                                }
                            }
                        },
                    )
                }
                MaterialTheme(
                    colors = MaterialTheme.colors.copy(
                        background = TransactionCardBg,
                        surface = TransactionCardBg,
                        onBackground = TransactionCardBg,
                    )
                ) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.exposedDropdownSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 26.dp),
                            horizontalArrangement = Arrangement.spacedBy(25.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CategoryFab(
                                icon = category.icon,
                                modifier = Modifier,
                                size = 35.dp,
                                colors = category.colors
                            )
                            Text(
                                text = category.category,
                                color = White,
                                fontFamily = DMSans,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Text(
                            text = "All Categories".uppercase(),
                            color = White.copy(0.5f),
                            fontFamily = DMSans,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(
                                start = 26.dp,
                                top = 20.dp,
                                bottom = 15.dp
                            )
                        )
                        Categories.values().forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(remember { MutableInteractionSource() }, null) {
                                        onSelect(it)
                                        expanded = !expanded
                                    }
                                    .padding(horizontal = 26.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(25.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CategoryFab(
                                    icon = it.icon,
                                    modifier = Modifier.clickable(
                                        remember { MutableInteractionSource() }, null
                                    ) {
                                        onSelect(it)
                                        expanded = !expanded
                                    },
                                    size = 35.dp,
                                    colors = it.colors
                                )
                                Text(
                                    text = it.category,
                                    fontFamily = DMSans,
                                    fontWeight = FontWeight.Bold,
                                    color = White,
                                    modifier = Modifier.clickable(
                                        remember { MutableInteractionSource() }, null
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