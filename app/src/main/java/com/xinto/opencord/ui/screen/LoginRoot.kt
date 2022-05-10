package com.xinto.opencord.ui.screen

import androidx.compose.animation.*
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.navigation.OCNavigation
import com.xinto.opencord.ui.navigation.LoginScreen
import com.xinto.opencord.ui.navigation.rememberOCNavigatorBackstack

@Composable
fun LoginRootScreen() {
    val navigator = rememberOCNavigatorBackstack<LoginScreen>(initial = LoginScreen.Landing)
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
        OCNavigation(
            navigator = navigator,
            transitionSpec = {
                when {
                    LoginScreen.Landing isTransitioningTo LoginScreen.Login -> {
                        slideIntoContainer(
                            towards = AnimatedContentScope.SlideDirection.Start,
                            initialOffset = { it }
                        ) with fadeOut() + slideOutOfContainer(
                            towards = AnimatedContentScope.SlideDirection.Start,
                            targetOffset = { it / 3 }
                        )
                    }
                    LoginScreen.Login isTransitioningTo LoginScreen.Landing -> {
                        fadeIn() + slideIntoContainer(
                            towards = AnimatedContentScope.SlideDirection.End,
                            initialOffset = { it / 3 }
                        ) with slideOutOfContainer(
                            towards = AnimatedContentScope.SlideDirection.End,
                            targetOffset = { it }
                        )
                    }
                    else -> fadeIn() with fadeOut()
                }

            },
            backPressEnabled = true,
            onBackPress = { navigator.back() }
        ) {
            when (it) {
                is LoginScreen.Landing -> {
                    LoginLandingScreen(
                        onLoginClick = {
                            navigator.navigate(LoginScreen.Login)
                        },
                        onRegisterClick = {}
                    )
                }
                is LoginScreen.Login -> {
                    LoginScreen(onBackClick = { navigator.back() })
                }
            }
        }
    }
}