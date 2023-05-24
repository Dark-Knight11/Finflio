package com.finflio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.cloudinary.android.MediaManager
import com.finflio.ui.theme.BottomNavBlue
import com.finflio.ui.theme.FinflioTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        // Cloudinary Configuration
        val config: MutableMap<String, String> = HashMap()
        config["cloud_name"] = BuildConfig.CLOUD_NAME
        config["api_key"] = BuildConfig.CLOUDINARY_API_KEY
        config["api_secret"] = BuildConfig.CLOUDINARY_API_SECRET
        MediaManager.init(this, config)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FinflioTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    // set transparent color so that our image is visible
                    // behind the status bar
                    systemUiController.setStatusBarColor(color = Color.Transparent)
                    systemUiController.setNavigationBarColor(color = BottomNavBlue.copy(0.1f))
                }
                val navHostEngine = rememberAnimatedNavHostEngine(
                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
                        enterTransition = { fadeIn(animationSpec = tween(1000)) },
                        exitTransition = { fadeOut(animationSpec = tween(1000)) }
                    )
                )
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    engine = navHostEngine
                )
            }
        }
    }
}
