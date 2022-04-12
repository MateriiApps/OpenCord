package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.ui.screen.LoginRootScreen
import com.xinto.opencord.ui.theme.OpenCordTheme

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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