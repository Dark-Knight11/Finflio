package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.finflio.R
import com.finflio.core.presentation.components.Grapple
import com.finflio.ui.theme.Poppins
import com.finflio.ui.theme.PrimaryText
import com.finflio.ui.theme.navigationBarHeight
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PermissionDrawer(
    drawerState: BottomDrawerState,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val gesturesEnabled by remember { derivedStateOf { drawerState.isOpen } }

    BackHandler(drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    BottomDrawer(
        drawerContent = {
            PermissionDrawerContent(drawerState, scope)
        },
//        drawerBackgroundColor = AttendanceBackgroundUpper,
        drawerElevation = 0.dp,
        drawerShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        scrimColor = Color.Black.copy(0.59f),
        drawerContentColor = PrimaryText,
        content = content
    )
}


@Composable
fun PermissionDrawerContent(
    drawerState: BottomDrawerState,
    scope: CoroutineScope,
) {
    val permissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(
            top = 15.dp,
            bottom = 10.dp + navigationBarHeight,
            start = 25.dp,
            end = 25.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Grapple(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .requiredHeight(10.dp)
                .requiredWidth(50.dp)
                .alpha(0.22f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = context.getIconForGallery(),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.FillWidth
            )
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .rotate(90f)
                    .size(40.dp),
//                tint = Blue100.copy(0.22f)
            )
            Image(
                painter = painterResource(R.drawable.google_pay),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        if (permissionState.status.shouldShowRationale)
            Text(
                text = "To select images, allow Companion to access your device's photos, media " +
                        "and files. Tap Settings > Permission, and turn \"Files and media\" on.",
                textAlign = TextAlign.Center,
                color = PrimaryText,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        else
            Text(
                text = "Storage permission required for this feature to be available. Please grant the permission.",
                textAlign = TextAlign.Center,
                color = PrimaryText,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

        Button(
            modifier = Modifier.padding(top = 5.dp),
            onClick = {
                if (permissionState.status.shouldShowRationale) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + context.packageName)
                    context.startActivity(intent)
                } else {
                    permissionState.launchPermissionRequest()
                }
                scope.launch { drawerState.close() }
            },
            shape = CircleShape
        ) {
            if (permissionState.status.shouldShowRationale)
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            else
                Text(
                    text = "Grant Permission",
                    color = Color.White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
        }
        Text(
            text = "Maybe Later",
//            color = BrightBlue,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { scope.launch { drawerState.close() } },
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Suppress("DEPRECATION")
@SuppressLint("IntentReset")
private fun Context.getIconForGallery(): Any {
    val packageManager = this.packageManager
    val galleryIntent = Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
    }

    val gallery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.queryIntentActivities(galleryIntent,
            PackageManager.ResolveInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong()))
    } else {
        packageManager.queryIntentActivities(galleryIntent, PackageManager.GET_ACTIVITIES)
    }
    return if (gallery.isEmpty()) R.drawable.gallery_icon else packageManager.getApplicationIcon(
        gallery[0].activityInfo.packageName)
}