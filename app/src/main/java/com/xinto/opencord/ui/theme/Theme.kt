package com.xinto.opencord.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DiscordBlurple,
    surface = DiscordDarkSurface,
    background = DiscordDarkBackground,
    error = DiscordRed
)

private val LightColorPalette = lightColors(
    primary = DiscordBlurple,
    surface = DiscordLightSurface,
    background = DiscordLightBackground,
    error = DiscordRed
)

val Colors.secondaryButtonColor get() = DiscordLightGray

@Composable
fun OpenCordTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}