package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.xinto.opencord.ui.screens.main.MainScreen
import com.xinto.opencord.ui.theme.OpenCordTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenCordTheme {
                MainScreen()
            }
        }
    }
}