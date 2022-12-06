package com.xinto.opencord.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = ColorLightPrimary,
    onPrimary = ColorLightOnPrimary,
    primaryContainer = ColorLightPrimaryContainer,
    onPrimaryContainer = ColorLightOnPrimaryContainer,
    secondary = ColorLightSecondary,
    onSecondary = ColorLightOnSecondary,
    secondaryContainer = ColorLightSecondaryContainer,
    onSecondaryContainer = ColorLightOnSecondaryContainer,
    tertiary = ColorLightTertiary,
    onTertiary = ColorLightOnTertiary,
    tertiaryContainer = ColorLightTertiaryContainer,
    onTertiaryContainer = ColorLightOnTertiaryContainer,
    error = ColorLightError,
    errorContainer = ColorLightErrorContainer,
    onError = ColorLightOnError,
    onErrorContainer = ColorLightOnErrorContainer,
    background = ColorLightBackground,
    onBackground = ColorLightOnBackground,
    surface = ColorLightSurface,
    onSurface = ColorLightOnSurface,
    surfaceVariant = ColorLightSurfaceVariant,
    onSurfaceVariant = ColorLightOnSurfaceVariant,
    outline = ColorLightOutline,
    inverseOnSurface = ColorLightInverseOnSurface,
    inverseSurface = ColorLightInverseSurface,
    inversePrimary = ColorLightInversePrimary,
    surfaceTint = ColorLightSurfaceTint,
    outlineVariant = ColorLightOutlineVariant,
    scrim = ColorLightScrim,
)

private val DarkColors = darkColorScheme(
    primary = ColorDarkPrimary,
    onPrimary = ColorDarkOnPrimary,
    primaryContainer = ColorDarkPrimaryContainer,
    onPrimaryContainer = ColorDarkOnPrimaryContainer,
    secondary = ColorDarkSecondary,
    onSecondary = ColorDarkOnSecondary,
    secondaryContainer = ColorDarkSecondaryContainer,
    onSecondaryContainer = ColorDarkOnSecondaryContainer,
    tertiary = ColorDarkTertiary,
    onTertiary = ColorDarkOnTertiary,
    tertiaryContainer = ColorDarkTertiaryContainer,
    onTertiaryContainer = ColorDarkOnTertiaryContainer,
    error = ColorDarkError,
    errorContainer = ColorDarkErrorContainer,
    onError = ColorDarkOnError,
    onErrorContainer = ColorDarkOnErrorContainer,
    background = ColorDarkBackground,
    onBackground = ColorDarkOnBackground,
    surface = ColorDarkSurface,
    onSurface = ColorDarkOnSurface,
    surfaceVariant = ColorDarkSurfaceVariant,
    onSurfaceVariant = ColorDarkOnSurfaceVariant,
    outline = ColorDarkOutline,
    inverseOnSurface = ColorDarkInverseOnSurface,
    inverseSurface = ColorDarkInverseSurface,
    inversePrimary = ColorDarkInversePrimary,
    surfaceTint = ColorDarkSurfaceTint,
    outlineVariant = ColorDarkOutlineVariant,
    scrim = ColorDarkScrim,
)

@Composable
fun OpenCordTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkMode -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}