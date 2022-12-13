package com.xinto.opencord.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.navigation.MainNavigation
import com.xinto.opencord.ui.screens.home.HomeScreen
import com.xinto.opencord.ui.screens.pins.PinsScreen
import com.xinto.taxi.Taxi
import com.xinto.taxi.rememberBackstackNavigator

@Composable
fun RootScreen() {
    val navigator = rememberBackstackNavigator<MainNavigation>(MainNavigation.Home)

    BackHandler {
        navigator.pop()
    }

    Taxi(
        navigator = navigator,
        transitionSpec = {
            if (initialState == MainNavigation.Home) {
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
            is MainNavigation.Home -> {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onSettingsClick = {
                        navigator.push(MainNavigation.Settings)
                    },
                    onPinsClick = {
                        navigator.push(MainNavigation.Pins(it))
                    },
                )
            }
            is MainNavigation.Pins -> {
                PinsScreen(
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = navigator::pop,
                )
            }
            is MainNavigation.Settings -> {
                Settings(
                    modifier = Modifier.fillMaxSize(),
                    onBackClick = navigator::pop,
                )
            }
        }
    }
}
