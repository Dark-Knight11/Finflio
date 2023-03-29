package com.finflio.feature_transactions.presentation.add_edit_transactions.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.finflio.ui.theme.SecondaryText

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    url: Uri? = null,
    link: String? = null,
    onClose: (() -> Unit)? = null,
) {
    Box(modifier) {
        if (link != null) {
            AsyncImage(
                model = link,
                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1.3f, true)
                    .clip(RoundedCornerShape(5.dp))
                    .border(1.dp, SecondaryText, RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1.3f, true)
                    .clip(RoundedCornerShape(5.dp))
                    .border(1.dp, SecondaryText, RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(5.dp)
                .clip(CircleShape)
                .clickable { onClose?.invoke() }
                .size(20.dp)
                .background(Color.Black.copy(0.45f))
                .padding(2.dp),
            tint = Color.White
        )
    }
}