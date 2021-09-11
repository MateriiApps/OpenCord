package com.xinto.opencord.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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

val Colors.secondaryButton
    get() = if (isLight) DiscordLightButtonColor else DiscordDarkButtonColor

val Typography.toolbar
    get() = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

@Composable
fun OpenCordTheme(
    content: @Composable () -> Unit,
) {
    val colors = if (isSystemInDarkTheme()) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}