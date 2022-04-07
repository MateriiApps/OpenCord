package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.ui.screen.MainScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.viewmodel.MainViewModel
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel = get()

    @OptIn(
        ExperimentalFoundationApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCordTheme {
                val systemUiController = rememberSystemUiController()
                val isLight = MaterialTheme.colors.isLight

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = isLight,
                    )
                }

                MainScreen()
            }
        }
    }
}