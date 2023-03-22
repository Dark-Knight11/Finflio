package com.finflio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.finflio.ui.theme.BottomNavBlue
import com.finflio.ui.theme.FinflioTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
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
                    ),
                    defaultAnimationsForNestedNavGraph = mapOf(
                        NavGraphs.main to NestedNavGraphDefaultAnimations(
                            enterTransition = { scaleIn(animationSpec = tween(2000)) },
                            exitTransition = { scaleOut(animationSpec = tween(2000)) }
                        )
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