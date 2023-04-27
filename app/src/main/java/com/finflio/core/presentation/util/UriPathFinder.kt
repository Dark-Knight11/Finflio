package com.finflio.core.presentation.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


class UriPathFinder {

    fun getPath(context: Context, uri: Uri): String? {
        val isKitKatorAbove = true

        // DocumentProvider
        if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {
                val dstPath =
                    context.cacheDir.absolutePath + File.separator.toString() + getFileName(context,
                        uri)
                if (copyFile(context, uri, dstPath)) {
                    return dstPath
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = uri?.let {
                context.contentResolver.query(it, projection, selection, selectionArgs, null)
            }
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}


fun getFileName(context: Context, uri: Uri?): String? {
    val cursor = uri?.let { context.contentResolver.query(it, null, null, null, null) }
    val nameindex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor?.moveToFirst()
    return nameindex?.let { cursor.getString(it) }
}
private fun copyFile(context: Context, uri: Uri, dstPath: String): Boolean {
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(uri)
        outputStream = FileOutputStream(dstPath)
        val buff = ByteArray(100 * 1024)
        var len: Int?
        while (inputStream?.read(buff).also {
                len = it
            } != -1) {
            len?.let { outputStream.write(buff, 0, it) }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    } finally {
        if (inputStream != null) {
            try {
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    return true
}
