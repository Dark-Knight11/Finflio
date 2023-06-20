package com.finflio

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinflioApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val config: MutableMap<String, String> = HashMap()
        config["cloud_name"] = BuildConfig.CLOUD_NAME
        config["api_key"] = BuildConfig.CLOUDINARY_API_KEY
        config["api_secret"] = BuildConfig.CLOUDINARY_API_SECRET
        MediaManager.init(this, config)
    }
}