package com.xinto.opencord.ui.screens.login

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.screens.login.navigation.LoginScreen
import com.xinto.taxi.Taxi
import com.xinto.taxi.rememberBackstackNavigator

@Composable
fun LoginRootScreen() {
    val navigator = rememberBackstackNavigator<LoginScreen>(initial = LoginScreen.Landing)
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
        Taxi(
            navigator = navigator,
            transitionSpec = {
                when {
                    LoginScreen.Landing isTransitioningTo LoginScreen.Login -> {
                        slideIntoContainer(
                            towards = AnimatedContentScope.SlideDirection.Start,
                            initialOffset = { it },
                        ) with fadeOut() + slideOutOfContainer(
                            towards = AnimatedContentScope.SlideDirection.Start,
                            targetOffset = { it / 3 },
                        )
                    }
                    LoginScreen.Login isTransitioningTo LoginScreen.Landing -> {
                        fadeIn() + slideIntoContainer(
                            towards = AnimatedContentScope.SlideDirection.End,
                            initialOffset = { it / 3 },
                        ) with slideOutOfContainer(
                            towards = AnimatedContentScope.SlideDirection.End,
                            targetOffset = { it },
                        )
                    }
                    else -> fadeIn() with fadeOut()
                }

            },
        ) {
            when (it) {
                is LoginScreen.Landing -> {
                    LoginLandingScreen(
                        onLoginClick = {
                            navigator.push(LoginScreen.Login)
                        },
                        onRegisterClick = {},
                    )
                }
                is LoginScreen.Login -> {
                    LoginScreen(onBackClick = navigator::pop)
                }
            }
        }
    }
}
