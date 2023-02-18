package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.ui.navigation.LoginDestinations
import com.xinto.opencord.ui.screens.login.LoginLandingScreen
import com.xinto.opencord.ui.screens.login.LoginScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import dev.olshevski.navigation.reimagined.NavHost
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
            val nav = rememberNavController<LoginDestinations>(startDestination = LoginDestinations.LoginLanding)

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
                    NavHost(controller = nav) { dest ->
                        when (dest) {
                            LoginDestinations.Login -> LoginScreen(
                                onBackClick = { nav.pop() },
                            )

                            LoginDestinations.LoginLanding -> LoginLandingScreen(
                                onLoginClick = { nav.navigate(LoginDestinations.Login) },
                                onRegisterClick = {},
                            )
                        }
                    }
                }
            }
        }
    }
}
