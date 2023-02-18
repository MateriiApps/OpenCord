package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.ui.navigation.AppDestinations
import com.xinto.opencord.ui.screens.Settings
import com.xinto.opencord.ui.screens.home.HomeScreen
import com.xinto.opencord.ui.screens.pins.PinsScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.viewmodel.MainViewModel
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.android.ext.android.get

class AppActivity : ComponentActivity() {
    private val viewModel: MainViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nav = rememberNavController<AppDestinations>(startDestination = AppDestinations.Main)

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

                NavHost(controller = nav) { dest ->
                    when (dest) {
                        AppDestinations.Main -> HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            onSettingsClick = { nav.navigate(AppDestinations.Settings) },
                            onPinsClick = { data: AppDestinations.Pins -> nav.navigate(data) },
                        )

                        AppDestinations.Settings -> Settings(
                            modifier = Modifier.fillMaxSize(),
                            onBackClick = { nav.pop() },
                        )

                        is AppDestinations.Pins -> PinsScreen(
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
