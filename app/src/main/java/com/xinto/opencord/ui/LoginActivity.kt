package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.ui.navigation.LoginDestination
import com.xinto.opencord.ui.screens.login.LoginLandingScreen
import com.xinto.opencord.ui.screens.login.LoginScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.rememberNavController
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Preload HCaptcha
        get<HCaptcha> { parametersOf(this) }

        setContent {
            val nav = rememberNavController<LoginDestination>(startDestination = LoginDestination.Landing)

            OpenCordTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = false
                val surface = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = surface,
                        darkIcons = useDarkIcons,
                    )
                }

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
                        when (dest) {
                            LoginDestination.Login -> LoginScreen(
                                onBackClick = { nav.pop() },
                            )

                            LoginDestination.Landing -> LoginLandingScreen(
                                onLoginClick = { nav.navigate(LoginDestination.Login) },
                                onRegisterClick = {},
                            )
                        }
                    }
                }
            }
        }
    }
}
