package com.xinto.opencord.rest.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ApiColor(private val color: Int) {
    val rgbColor get() = color and 0xFFFFFF

    val red: Int get() = (rgbColor shr 16) and 0xFF
    val green: Int get() = (rgbColor shr 8) and 0xFF
    val blue: Int get() = (rgbColor shr 0) and 0xFF
}

fun Color.toApi(): ApiColor {
    val color = ((red.toInt() and 0xFF) shl 16) or
            ((green.toInt() and 0xFF) shl 8) or
            (blue.toInt() and 0xFF)

    return ApiColor(color)
}
