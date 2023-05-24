package com.finflio.feature_transactions.presentation.add_edit_transactions.util

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        /* prefix = */ imageFileName,
        /* suffix = */ ".jpg",
        /* directory = */ externalCacheDir
    )
}
