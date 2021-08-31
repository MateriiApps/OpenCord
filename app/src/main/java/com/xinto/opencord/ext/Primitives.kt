package com.xinto.opencord.ext

fun Float.equalsAny(
    vararg others: Float
) = others.any { this == it }

fun Int.equalsAny(
    vararg others: Int
) = others.any { this == it }