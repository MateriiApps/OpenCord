package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.ui.navigation.LoginDestination
import com.xinto.opencord.ui.navigation.back
import com.xinto.opencord.ui.screens.login.LoginLandingScreen
import com.xinto.opencord.ui.screens.login.LoginScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.util.SystemBarsControl
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class LoginActivity : AppCompatActivity() {
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        scope.launch(Dispatchers.IO) {
            // Preload modules
            get<HCaptcha> { parametersOf(this@LoginActivity) }
            get<AccountDatabase>()
        }

        setContent {
            val nav = rememberNavController<LoginDestination>(startDestination = LoginDestination.Landing)

            BackHandler { nav.back() }

            OpenCordTheme {
                SystemBarsControl()

                CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
                    AnimatedNavHost(
                        controller = nav,
                        emptyBackstackPlaceholder = { nav.navigate(LoginDestination.Landing) },
                        transitionSpec = { _, from, to ->
                            when {
                                from == LoginDestination.Landing && to == LoginDestination.Login -> {
                                    slideIntoContainer(
                                        towards = AnimatedContentScope.SlideDirection.Start,
                                        initialOffset = { it },
                                    ) with fadeOut() + slideOutOfContainer(
                                        towards = AnimatedContentScope.SlideDirection.Start,
                                        targetOffset = { it / 3 },
                                    )
                                }
                                from == LoginDestination.Login && to == LoginDestination.Landing -> {
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
                    ) { dest ->
                        val uriHandler = LocalUriHandler.current

                        when (dest) {
                            LoginDestination.Login -> LoginScreen(
                                onBackClick = { nav.back() },
                            )

                            LoginDestination.Landing -> LoginLandingScreen(
                                onLoginClick = { nav.navigate(LoginDestination.Login) },
                                onRegisterClick = {
                                    uriHandler.openUri("https://discord.com/register")
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
