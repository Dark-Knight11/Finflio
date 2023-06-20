package com.finflio

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.finflio.core.data.model.JwtToken
import com.finflio.core.data.util.SessionManager
import com.finflio.destinations.BaseScreenDestination
import com.finflio.feature_authentication.presentation.AuthViewModel
import com.finflio.ui.theme.BottomNavBlue
import com.finflio.ui.theme.FinflioTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sessionManager.getUserData().collectLatest { userSettings ->
                    println("userSettings: $userSettings")
                    // check if token is expired
                    val jwtToken = userSettings.token?.split(".")
                    val currentDate = LocalDate.now()
                    jwtToken?.let {
                        val token =
                            Json.decodeFromString(JwtToken.serializer(), getData(jwtToken[1]))
                        val exp = LocalDateTime.ofEpochSecond(token.exp.toLong(), 0, ZoneOffset.UTC)
                            .toLocalDate()
                        if (currentDate >= exp) sessionManager.clearDatastore()
                    }
                    setContent {
                        FinflioTheme {
                            val systemUiController = rememberSystemUiController()
                            SideEffect {
                                // set transparent color so that our image is visible
                                // behind the status bar
                                systemUiController.setStatusBarColor(color = Color.Transparent)
                                systemUiController.setNavigationBarColor(
                                    color = BottomNavBlue.copy(0.1f)
                                )
                            }
                            val navHostEngine = rememberAnimatedNavHostEngine(
                                rootDefaultAnimations = RootNavGraphDefaultAnimations(
                                    enterTransition = { fadeIn(animationSpec = tween(1000)) },
                                    exitTransition = { fadeOut(animationSpec = tween(1000)) }
                                )
                            )

                            DestinationsNavHost(
                                navGraph = NavGraphs.root,
                                engine = navHostEngine,
                                dependenciesContainerBuilder = {
                                    dependency(NavGraphs.auth) {
                                        val parentEntry = remember(navBackStackEntry) {
                                            navController.getBackStackEntry(NavGraphs.auth.route)
                                        }
                                        hiltViewModel<AuthViewModel>(parentEntry)
                                    }
                                },
                                startRoute = if (userSettings.token.isNullOrBlank()) {
                                    NavGraphs.auth
                                } else {
                                    BaseScreenDestination
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getData(strEncoded: String): String {
        val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes)
    }
}