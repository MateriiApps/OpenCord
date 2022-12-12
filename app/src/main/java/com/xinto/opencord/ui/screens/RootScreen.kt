package com.xinto.opencord.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.navigation.MainScreen
import com.xinto.opencord.ui.screens.home.HomeScreen
import com.xinto.opencord.ui.screens.pins.PinsScreen
import com.xinto.taxi.Taxi
import com.xinto.taxi.rememberBackstackNavigator

@Composable
fun RootScreen() {
    val navigator = rememberBackstackNavigator<MainScreen>(MainScreen.Home)

    BackHandler {
        navigator.pop()
    }

    Taxi(
        navigator = navigator,
        transitionSpec = {
            if (initialState == MainScreen.Home) {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Start,
                    initialOffset = { it },
                ) with fadeOut() + slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Start,
                    targetOffset = { it / 3 },
                )
            } else {
                fadeIn() + slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.End,
                    initialOffset = { it / 3 },
                ) with slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.End,
                    targetOffset = { it },
                )
            }
        },
    ) { mainScreen ->
        when (mainScreen) {
            is MainScreen.Home -> {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onSettingsClick = {
                        navigator.push(MainScreen.Settings)
                    },
                    onPinsClick = {
                        navigator.push(MainScreen.Pins(it))
                    },
                )
            }
            is MainScreen.Pins -> {
                PinsScreen(
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = navigator::pop,
                )
            }
            is MainScreen.Settings -> {
                Settings(
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = navigator::pop,
                )
            }
        }
    }
}
