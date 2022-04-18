package com.xinto.opencord.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.navigation.MainScreen
import com.xinto.opencord.ui.navigation.OCNavigation
import com.xinto.opencord.ui.navigation.rememberOCNavigatorBackstack

@Composable
fun MainRootScreen() {
    val navigator = rememberOCNavigatorBackstack<MainScreen>(initial = MainScreen.Home)
    OCNavigation(
        modifier = Modifier.fillMaxSize(),
        navigator = navigator,
        transitionSpec = {
            if (initialState == MainScreen.Home) {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Start,
                    initialOffset = { it }
                ) with fadeOut() + slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Start,
                    targetOffset = { it / 3 }
                )
            } else {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.End,
                    initialOffset = { it / 3 }
                ) with slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.End,
                    targetOffset = { it }
                )
            }
        },
        backPressEnabled = true,
        onBackPress = { navigator.back() }
    ) {
        when (it) {
            is MainScreen.Home -> {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onSettingsClick = {
                        navigator.navigate(MainScreen.Settings)
                    },
                    onPinsClick = {
                        navigator.navigate(MainScreen.Pins)
                    }
                )
            }
            is MainScreen.Pins -> {
                ChannelPinsScreen(
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = {
                        navigator.back()
                    }
                )
            }
            is MainScreen.Settings -> {
                Settings(
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = {
                        navigator.back()
                    }
                )
            }
        }
    }
}