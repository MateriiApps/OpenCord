package com.xinto.opencord.ui.util

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun rememberGestureNavEnabled(): Boolean {
    val context = LocalContext.current

    @SuppressLint("DiscouragedApi")
    val navBarModeId = remember {
        context.resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
    }

    return remember(context) {
        val navBarMode = if (navBarModeId <= 0) null else {
            context.resources.getInteger(navBarModeId)
        }

        // If mode is gesture nav
        navBarMode == 2
    }
}

/**
 * Accounts for the system's navigation mode,
 * if gestures are enabled then no bottom padding is applied,
 * otherwise for any other mode the full PaddingValues are used.
 */
fun Modifier.paddingOrGestureNav(paddingValues: PaddingValues): Modifier = composed {
    val isGestureNavEnabled = rememberGestureNavEnabled()

    if (isGestureNavEnabled) {
        padding(VoidablePaddingValues(paddingValues, bottom = false))
    } else {
        padding(paddingValues)
    }
}

/**
 * Controls the color of the system bars,
 * accounting for gesture navigation
 */
@Composable
fun SystemBarsControl(
    color: Color = MaterialTheme.colorScheme.surface,
    isLight: Boolean = false,
) {
    val systemUiController = rememberSystemUiController()
    val isGestureEnabled = rememberGestureNavEnabled()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = isLight,
        )

        if (isGestureEnabled) {
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
            )
        }
    }
}
