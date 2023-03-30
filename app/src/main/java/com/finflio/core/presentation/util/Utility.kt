package com.finflio.core.presentation.util

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

val Number.toDp get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).dp

val Number.toPx get() = (this.toFloat() * Resources.getSystem().displayMetrics.density)