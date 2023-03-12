package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.ui.navigation.AppDestination
import com.xinto.opencord.ui.navigation.back
import com.xinto.opencord.ui.screens.Settings
import com.xinto.opencord.ui.screens.home.HomeScreen
import com.xinto.opencord.ui.screens.pins.PinsScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.util.rememberGestureNavEnabled
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class AppActivity : ComponentActivity() {
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        scope.launch(Dispatchers.IO) {
            get<DiscordGateway>().connect()
        }

        scope.launch(Dispatchers.IO) {
            get<CacheDatabase>().apply {
                messages().clear()
                embeds().clear()
                reactions().clear()
                attachments().clear()
                users().deleteUnusedUsers()
            }
        }

        setContent {
            val nav = rememberNavController<AppDestination>(startDestination = AppDestination.Main)

            BackHandler { nav.back() }

            OpenCordTheme {
                val systemUiController = rememberSystemUiController()
                val isGestureEnabled = rememberGestureNavEnabled()
                val surface = MaterialTheme.colorScheme.surface
                val isLight = false

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = surface,
                        darkIcons = isLight,
                    )

                    if (isGestureEnabled) {
                        systemUiController.setNavigationBarColor(
                            color = Color.Transparent,
                        )
                    }
                }

                AnimatedNavHost(
                    controller = nav,
                    emptyBackstackPlaceholder = { nav.navigate(AppDestination.Main) },
                    transitionSpec = { _, _, to ->
                        if (to == AppDestination.Main) {
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
                ) { dest ->
                    when (dest) {
                        AppDestination.Main -> HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            onSettingsClick = { nav.navigate(AppDestination.Settings) },
                            onPinsClick = { nav.navigate(AppDestination.Pins(data = it)) },
                        )

                        AppDestination.Settings -> Settings(
                            modifier = Modifier.fillMaxSize(),
                            onBackClick = { nav.back() },
                        )

                        is AppDestination.Pins -> PinsScreen(
                            data = dest.data,
                            modifier = Modifier.fillMaxSize(),
                            onBackClick = { nav.back() },
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        scope.launch(Dispatchers.IO) {
            get<DiscordGateway>().disconnect()
        }

        super.onDestroy()
    }
}
