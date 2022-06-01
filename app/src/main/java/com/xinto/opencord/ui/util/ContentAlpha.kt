package com.xinto.opencord.ui.util

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object ContentAlpha {

    val full: Float
        @Composable
        @ReadOnlyComposable
        get() = 1.0f

    val extraHigh: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.90f

    val veryHigh: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.80f

    val high: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.70f

    val medium: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.60f

    val low: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.50f

    val veryLow: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.40f

    val extraLow: Float
        @Composable
        @ReadOnlyComposable
        get() = 0.30f
}

@Composable
fun ProvideContentAlpha(value: Float, content: @Composable () -> Unit) {
    val opaqueColor = LocalContentColor.current.copy(alpha = value)
    CompositionLocalProvider(
        LocalContentColor provides opaqueColor,
        content = content
    )
}