package com.finflio.core.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.finflio.NavGraphs
import com.finflio.R
import com.finflio.appCurrentDestinationAsState
import com.finflio.core.presentation.components.BottomNav
import com.finflio.core.presentation.components.DiamondFab
import com.finflio.core.presentation.components.FabWithoutIndication
import com.finflio.core.presentation.util.toPx
import com.finflio.destinations.AddEditTransactionScreenDestination
import com.finflio.destinations.ListTransactionsDestination
import com.finflio.destinations.ShowStatsDestination
import com.finflio.ui.theme.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RootNavGraph(true)
@Destination()
@Composable
fun BaseScreen(navigator: DestinationsNavigator) {
    val navController = rememberAnimatedNavController()
    var fabVisibility by remember { mutableStateOf(false) }
    val degree = remember { mutableStateOf(45f) }
    val rotate = remember { mutableStateOf(-90f) }
    if (!fabVisibility) {
        degree.value = 45f
        rotate.value = -90f
    } else {
        degree.value = 0f
        rotate.value = 0f
    }
    val button: Float by animateFloatAsState(
        targetValue = rotate.value,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    )
    val angle: Float by animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    )
    val currentDestination = navController.appCurrentDestinationAsState().value
    val routesWithBottomNav = listOf(ListTransactionsDestination, ShowStatsDestination)
    val bottomBarVisibility = currentDestination in routesWithBottomNav
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisibility,
                enter = slideInVertically(animationSpec = tween(),
                    initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(animationSpec = tween(),
                    targetOffsetY = { it }) + fadeOut()
            ) {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp + navigationBarHeight),
                    backgroundColor = Color.Transparent,
                    elevation = 22.dp,
                    cutoutShape = DiamondShape(86.dp.toPx()),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    BottomNav(navController)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            AnimatedVisibility(
                visible = bottomBarVisibility,
                enter = slideInVertically(animationSpec = tween(),
                    initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(animationSpec = tween(),
                    targetOffsetY = { it }) + fadeOut()
            ) {
                DiamondFab(
                    modifier = Modifier.rotate(button),
                    onClick = { fabVisibility = !fabVisibility },
                    size = 56.dp,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Show Options",
                            tint = Color.White,
                            modifier = Modifier.rotate(angle)
                        )
                    }
                )
            }
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground(
                        colorStops = arrayOf(
                            0.0f to Gold.copy(0.5f),
                            0.2f to MainBackground
                        ),
                        angle = -70f,
                        extraY = -50.dp.toPx()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                DestinationsNavHost(
                    navController = navController,
                    navGraph = NavGraphs.main
                )
            }
            AnimatedVisibility(
                visible = fabVisibility,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.5f))
                        .gesturesDisabled()
                )
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = fabVisibility,
                    enter = slideIn(
                        animationSpec = tween(300),
                        initialOffset = {
                            IntOffset(0,0)
                        }
                    ) + scaleIn(),
                    exit = slideOut(
                        animationSpec = tween(),
                        targetOffset = {
                            IntOffset(0,0)
                        }
                    ) + scaleOut()
                ) {
                    FabWithoutIndication(
                        modifier = Modifier
                            .rotate(button)
                            .size(62.dp)
                            .graphicsLayer {
                                shape = DiamondShape(62.dp.toPx())
                                clip = true
                            }
                            .background(TransferBlue),
                        onClick = { navigator.navigate(AddEditTransactionScreenDestination(type = "Unsettled")) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_transfer),
                            contentDescription = "tranfer",
                            tint = Color.White
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 140.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    AnimatedVisibility(
                        visible = fabVisibility,
                        enter = slideIn(
                            animationSpec = tween(
                                durationMillis = 300
                            ),
                            initialOffset = {
                                IntOffset(it.width, it.height)
                            }
                        ) + fadeIn() + scaleIn(),
                        exit = slideOut(
                            animationSpec = tween(),
                            targetOffset = {
                                IntOffset(it.width, it.height)
                            }
                        ) + fadeOut() + scaleOut()
                    ) {
                        FabWithoutIndication(
                            modifier = Modifier
                                .rotate(button)
                                .size(62.dp)
                                .graphicsLayer {
                                    shape = DiamondShape(62.dp.toPx())
                                    clip = true
                                }
                                .background(Income),
                            onClick = { navigator.navigate(AddEditTransactionScreenDestination(type = "Income")) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_income),
                                contentDescription = "income",
                                tint = Color.White
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = fabVisibility,
                        enter = slideIn(
                            animationSpec = tween(),
                            initialOffset = {
                                IntOffset(-it.width, it.height)
                            }
                        ) + fadeIn() + scaleIn(),
                        exit = slideOut(
                            animationSpec = tween(300),
                            targetOffset = {
                                IntOffset(-it.width, it.height)
                            }
                        ) + fadeOut() + scaleOut()
                    ) {
                        FabWithoutIndication(
                            modifier = Modifier
                                .rotate(-button)
                                .size(62.dp)
                                .graphicsLayer {
                                    shape = DiamondShape(62.dp.toPx())
                                    clip = true
                                }
                                .background(Expense),
                            onClick = { navigator.navigate(AddEditTransactionScreenDestination(type = "Expense")) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_expense),
                                contentDescription = "expense",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}