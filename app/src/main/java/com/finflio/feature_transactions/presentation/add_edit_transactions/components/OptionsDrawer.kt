package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.R
import com.finflio.core.presentation.components.Grapple
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.ExpenseBG
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.IncomeBG
import com.finflio.ui.theme.MainBackground
import com.finflio.ui.theme.PrimaryText
import com.finflio.ui.theme.navigationBarHeight
import kotlinx.coroutines.launch

@Composable
fun OptionsDrawer(
    optionsDrawerState: BottomDrawerState,
    type: String,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val gesturesEnabled = remember { derivedStateOf { optionsDrawerState.isOpen } }

    BackHandler(optionsDrawerState.isOpen) {
        scope.launch {
            optionsDrawerState.close()
        }
    }

    BottomDrawer(
        drawerContent = {
            OptionsDrawerContent(
                type = type,
                onCameraClicked = onCameraClicked,
                onGalleryClicked = onGalleryClicked
            )
        },
        drawerElevation = 0.dp,
        drawerShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        drawerState = optionsDrawerState,
        gesturesEnabled = gesturesEnabled.value,
        scrimColor = Color.Black.copy(0.2f),
        drawerContentColor = PrimaryText,
        content = content
    )
}


@Composable
fun OptionsDrawerContent(
    type: String,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                0.0f to MainBackground,
                1.0f to if (type == "Expense") ExpenseBG.copy(0.9f)
                else IncomeBG.copy(0.9f)
            )
        )
    ) {
        Grapple(
            modifier = Modifier
                .padding(bottom = 10.dp, top = 10.dp)
                .requiredHeight(10.dp)
                .requiredWidth(50.dp)
                .align(Alignment.CenterHorizontally)
                .alpha(0.22f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = navigationBarHeight)
                .padding(horizontal = 70.dp, vertical = 25.dp)
        ) {
            Option(text = "Camera", icon = R.drawable.camera) {
                onCameraClicked()
            }
            Option(text = "Gallery", icon = R.drawable.gallery) {
                onGalleryClicked()
            }
        }
    }
}

@Composable
fun Option(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(remember { MutableInteractionSource() }, null) {
            onClick()
        },
        verticalArrangement = Arrangement.spacedBy(7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .border(
                    BorderStroke(1.dp, GoldIcon),
                    shape = CircleShape
                )
                .padding(5.dp),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = text,
            fontFamily = DMSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}