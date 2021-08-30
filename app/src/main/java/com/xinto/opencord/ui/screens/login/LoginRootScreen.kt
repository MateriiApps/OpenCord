package com.xinto.opencord.ui.screens.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.xinto.opencord.ui.widgets.navigation.OpenCordNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginRootScreen() {
    val navController = rememberAnimatedNavController()
    Surface(
        color = MaterialTheme.colors.background
    ) {
        OpenCordNavHost(
            navController = navController,
            startDestination = "landing",
        ) {
            composable("landing") {
                LoginLandingScreen(navController)
            }
            composable("login") {
                LoginScreen(navController)
            }
        }
    }
}