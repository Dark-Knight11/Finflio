package com.finflio.feature_authentication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finflio.R
import com.finflio.core.presentation.components.CommonSnackBar
import com.finflio.core.presentation.navigation.AuthNavGraph
import com.finflio.destinations.RegisterScreenDestination
import com.finflio.feature_authentication.presentation.components.AuthButton
import com.finflio.feature_authentication.presentation.components.AuthTextField
import com.finflio.feature_authentication.presentation.utils.AuthEvents
import com.finflio.feature_authentication.presentation.utils.AuthUiEvents
import com.finflio.ui.theme.DMSans
import com.finflio.ui.theme.ExpenseBG
import com.finflio.ui.theme.GoldIcon
import com.finflio.ui.theme.MainBackground
import com.finflio.ui.theme.Syne
import com.finflio.ui.theme.gradientBackground
import com.finflio.ui.theme.navigationBottomBarHeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@AuthNavGraph(true)
@Destination
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val annotatedString = buildAnnotatedString {
        append(
            AnnotatedString(
                text = "Don’t have a account? ",
                spanStyle = SpanStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontFamily = DMSans,
                    fontSize = 17.sp
                )
            )
        )

        pushStringAnnotation("Register", "Register")
        append(
            AnnotatedString(
                text = "Register",
                spanStyle = SpanStyle(
                    color = GoldIcon,
                    fontWeight = FontWeight.Bold,
                    fontFamily = DMSans,
                    fontSize = 17.sp
                )
            )
        )
        pop()
    }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AuthUiEvents.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    CommonSnackBar(
        snackBarHostState = snackbarHostState,
        modifier = Modifier.padding(bottom = navigationBottomBarHeight)
    ) {
        Scaffold(
            bottomBar = {
                ClickableText(
                    text = annotatedString,
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = navigationBottomBarHeight + 30.dp),
                    onClick = {
                        annotatedString.getStringAnnotations(
                            tag = "Register",
                            start = it,
                            end = it
                        ).firstOrNull().let { annotation ->
                            annotation?.item?.let {
                                navigator.popBackStack()
                                navigator.navigate(RegisterScreenDestination)
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground(
                        colorStops = arrayOf(
                            0.4f to MainBackground,
                            1.0f to ExpenseBG
                        ),
                        angle = -70f
                    )
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Login",
                    fontFamily = Syne,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = GoldIcon
                )
                Spacer(modifier = Modifier.height(35.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .graphicsLayer {
                            shape = RoundedCornerShape(20.dp)
                            clip = true
                        }
                        .background(Color.White.copy(0.1f))
                        .padding(20.dp)
                        .padding(bottom = 15.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        AuthTextField(
                            text = email,
                            placeholder = "Email",
                            leadingIcon = R.drawable.ic_at_sign,
                            onTextChange = { email = it }
                        )
                        AuthTextField(
                            text = password,
                            placeholder = "Password",
                            leadingIcon = R.drawable.ic_key,
                            trailingIcon = {
                                if (passwordVisibility) {
                                    R.drawable.ic_visibility_off
                                } else {
                                    R.drawable.ic_visibility_on
                                }
                            },
                            transformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                            onTextChange = { password = it },
                            toggleVisibility = { passwordVisibility = !passwordVisibility }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(35.dp))
                AuthButton(text = "Login") {
                    viewModel.onEvent(AuthEvents.Login(email, password))
                }
            }
        }
    }
}