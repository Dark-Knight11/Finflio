package com.finflio.feature_transactions.domain.use_case

import android.content.Context
import android.net.Uri
import com.finflio.core.presentation.util.UriPathFinder
import javax.inject.Inject

class GetImagePathUseCase @Inject constructor(
    private val uriPathFinder: UriPathFinder
) {
    operator fun invoke(list: Uri, context: Context): String? {
        return uriPathFinder.getPath(context, list)
    }
}