package com.xinto.opencord.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hcaptcha.sdk.HCaptcha
import com.hcaptcha.sdk.HCaptchaConfig
import com.hcaptcha.sdk.HCaptchaTheme
import com.xinto.opencord.ui.screen.LoginRootScreen
import com.xinto.opencord.ui.theme.OpenCordTheme

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCordTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = false
                val useDarkTheme = isSystemInDarkTheme()
                val surface = MaterialTheme.colorScheme.surface

                val hCaptchaConfig = HCaptchaConfig.builder()
                    .siteKey("f5561ba9-8f1e-40ca-9b5b-a0b3f719ef34") // doubt this will ever change
                    .resetOnTimeout(true)
                    .theme(if (useDarkTheme) HCaptchaTheme.DARK else HCaptchaTheme.LIGHT)
                    .build()
                hCaptcha.setup(hCaptchaConfig)

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

val Context.hCaptcha: HCaptcha
    get() = HCaptcha.getClient(this)