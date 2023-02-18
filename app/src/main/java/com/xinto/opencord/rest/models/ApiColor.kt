package com.xinto.opencord.rest.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ApiColor(val internalColor: Int) {
    val fullArgbColor get() = internalColor or (0xFF shl 24)
//    val rgbColor get() = color and 0xFFFFFF

//    val red: Int get() = (rgbColor shr 16) and 0xFF
//    val green: Int get() = (rgbColor shr 8) and 0xFF
//    val blue: Int get() = (rgbColor shr 0) and 0xFF
}

fun Color.toApi(): ApiColor = ApiColor(toArgb())

fun ApiColor.toColor(): Color = Color(fullArgbColor)
