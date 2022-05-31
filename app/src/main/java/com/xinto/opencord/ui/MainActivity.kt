package com.xinto.opencord.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xinto.opencord.rest.dto.ApiMessagePartial
import com.xinto.opencord.rest.dto.ApiSnowflake
import com.xinto.opencord.ui.screen.MainRootScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.partialgen.PartialValue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val d = ApiMessagePartial(id = PartialValue.Value(ApiSnowflake(0UL)))
        val t = Json.encodeToString(d)
        Log.i("t", t)
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

                MainRootScreen()
            }
        }
    }
}