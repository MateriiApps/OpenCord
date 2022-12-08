package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hcaptcha.sdk.HCaptcha
import com.xinto.opencord.ui.screen.LoginRootScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        get<HCaptcha> { parametersOf(this) } //load client at start
        setContent {
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

                LoginRootScreen()
            }
        }
    }
}
