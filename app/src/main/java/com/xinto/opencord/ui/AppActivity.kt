package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.ui.navigation.AppDestination
import com.xinto.opencord.ui.screens.Settings
import com.xinto.opencord.ui.screens.home.HomeScreen
import com.xinto.opencord.ui.screens.pins.PinsScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.viewmodel.MainViewModel
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.android.ext.android.get

class AppActivity : ComponentActivity() {
    private val viewModel: MainViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nav = rememberNavController<AppDestination>(startDestination = AppDestination.Main)

            OpenCordTheme {
                val systemUiController = rememberSystemUiController()
                val isLight = false
                val surface = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = surface,
                        darkIcons = isLight,
                    )
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
                            onBackClick = { nav.pop() },
                        )

                        is AppDestination.Pins -> PinsScreen(
                            data = dest.data,
                            modifier = Modifier.fillMaxSize(),
                            onBackClick = { nav.pop() },
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        get<CacheDatabase>().close()

        super.onDestroy()
    }
}
