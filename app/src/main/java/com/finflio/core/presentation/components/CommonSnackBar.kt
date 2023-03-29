package com.finflio.core.presentation.components

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.finflio.ui.theme.Gold
import com.finflio.ui.theme.MainBackground

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CommonSnackBar(snackBarHostState: SnackbarHostState, modifier: Modifier, content: @Composable () -> Unit) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) {
                Snackbar(
                    contentColor = Color.White,
                    backgroundColor = MainBackground,
                    actionColor = Gold,
                    modifier = modifier,
                    snackbarData = it
                )
            }
        },
        backgroundColor = Color.Transparent,
        content = { content() }
    )
}