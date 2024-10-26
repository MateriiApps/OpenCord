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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.ui.navigation.AppDestination
import com.xinto.opencord.ui.screens.ImageViewerScreen
import com.xinto.opencord.ui.screens.Settings
import com.xinto.opencord.ui.screens.home.HomeScreen
import com.xinto.opencord.ui.screens.mentions.MentionsScreen
import com.xinto.opencord.ui.screens.pins.PinsScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.util.SystemBarsControl
import com.xinto.opencord.ui.viewmodel.NavigationViewModel
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.compose.getViewModel

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
            OpenCordTheme {
                SystemBarsControl()

                val nav = getViewModel<NavigationViewModel>(extras = defaultViewModelCreationExtras)

                BackHandler { nav.back() }

                AnimatedNavHost(
                    backstack = nav.backstack,
                    emptyBackstackPlaceholder = { nav.resetToMain() },
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
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding(),
                        )

                        AppDestination.Settings -> Settings(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding(),
                            onBackClick = { nav.back() },
                        )

                        AppDestination.Mentions -> MentionsScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding(),
                            onBackClick = { nav.back() },
                        )

                        is AppDestination.Pins -> PinsScreen(
                            data = dest.data,
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding(),
                            onBackClick = { nav.back() },
                        )

                        is AppDestination.ImageViewer -> ImageViewerScreen(
                            data = dest.data,
                            onBackClick = { nav.back() },
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding(),
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
