package com.finflio.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finflio.NavGraphs
import com.finflio.appCurrentDestinationAsState
import com.finflio.core.presentation.util.BottomBarDestination
import com.finflio.destinations.Destination
import com.finflio.startAppDestination
import com.finflio.ui.theme.*
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

@Composable
fun BottomNav(navController: NavController) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.main.startAppDestination
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        BottomNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to BottomNavBlue.copy(0.7f),
                        0.4f to BottomNavBlue
                    )
                )
                .padding(bottom = 40.dp),
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            BottomBarDestination.values().forEach { destination ->
                BottomNavigationItem(
                    modifier = if (destination.direction.startAppDestination == currentDestination) Modifier.glow(true, Gold) else Modifier,
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.icon),
                            contentDescription = "",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            text = destination.label,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    selected = currentDestination == destination.direction.startAppDestination,
                    selectedContentColor = GoldIcon,
                    unselectedContentColor = Color.White.copy(alpha = 0.4f),
                    onClick = {
                        navController.navigate(destination.direction) {
                            // Pop up to the root of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(NavGraphs.main) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}