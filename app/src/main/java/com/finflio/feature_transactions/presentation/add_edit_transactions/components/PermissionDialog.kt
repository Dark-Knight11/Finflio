package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.finflio.R
import com.finflio.core.presentation.navigation.HomeNavGraph
import com.finflio.ui.theme.Expense
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.Income
import com.finflio.ui.theme.MainBackground
import com.finflio.ui.theme.Poppins
import com.finflio.ui.theme.PrimaryText
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.shouldShowRationale

@HomeNavGraph
@Composable
fun PermissionDialog(
    permissionState: PermissionState,
    context: Context,
    type: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    shape = RoundedCornerShape(25.dp)
                    clip = true
                }
                .background(MainBackground),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (type == "Expense") Expense.copy(alpha = 0.3f) else Income.copy(alpha = 0.3f))
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = null,
                        modifier = Modifier
                            .size(45.dp)
                            .border(
                                BorderStroke(1.dp, GoldIcon),
                                shape = CircleShape
                            )
                            .padding(3.dp),
                        contentScale = ContentScale.FillWidth
                    )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(90f)
                            .size(40.dp)
                    )
                    // TODO replace with app logo
                    Image(
                        painter = painterResource(R.drawable.google_pay),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            if (permissionState.status.shouldShowRationale)
                Text(
                    text = "To capture photos allow Finflio access to your camera",
                    textAlign = TextAlign.Start,
                    color = PrimaryText,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(15.dp)
                )
            else
                Text(
                    text = "Camera permission required for this feature to be available. Please grant the permission.",
                    textAlign = TextAlign.Justify,
                    color = PrimaryText,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 35.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Not Now",
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDismiss() },
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(Modifier.width(25.dp))
                Text(
                    text = if (permissionState.status.shouldShowRationale) "Settings" else "Continue",
                    color = GoldIcon,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(
                        remember { MutableInteractionSource() },
                        null
                    ) {
                        if (permissionState.status.shouldShowRationale) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:" + context.packageName)
                            context.startActivity(intent)
                        } else {
                            permissionState.launchPermissionRequest()
                            onDismiss()
                        }
                    }
                )
            }
        }
    }
}