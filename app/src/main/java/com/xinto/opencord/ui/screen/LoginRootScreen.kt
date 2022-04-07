package com.xinto.opencord.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.xinto.opencord.ui.component.OCNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginRootScreen() {
    val navController = rememberAnimatedNavController()
    Surface(
        color = MaterialTheme.colors.background
    ) {
        OCNavHost(
            navController = navController,
            startDestination = "landing",
        ) {
            composable("landing") {
                LoginLandingScreen(
                    onLoginClick = {
                        navController.navigate("login")
                    },
                    onRegisterClick = {}
                )
            }
            composable("login") {
                LoginScreen(onBackClick = {
                    navController.popBackStack()
                })
            }
        }
    }
}