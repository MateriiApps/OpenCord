package com.xinto.opencord.rest.dto

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ApiColor(private val color: Int) {

    val rgbColor get() = color and 0xFFFFFF

    val red: Int get() = (rgbColor shr 16) and 0xFF
    val green: Int get() = (rgbColor shr 8) and 0xFF
    val blue: Int get() = (rgbColor shr 0) and 0xFF

}