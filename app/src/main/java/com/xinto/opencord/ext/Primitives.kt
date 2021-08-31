package com.xinto.opencord.ext

fun Float.equalsAny(
    vararg other: Float
) = other.any { this == it }

fun Int.equalsAny(
    vararg other: Int
) = other.any { this == it }