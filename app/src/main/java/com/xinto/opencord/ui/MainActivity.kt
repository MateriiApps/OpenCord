package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.ui.screens.RootScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCordTheme(darkMode = true) {
                val systemUiController = rememberSystemUiController()
                val isLight = false
                val surface = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = surface,
                        darkIcons = isLight,
                    )
                }

                RootScreen()
            }
        }
    }
}
