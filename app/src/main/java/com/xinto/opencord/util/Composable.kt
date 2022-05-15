package com.xinto.opencord.util

import androidx.compose.runtime.Composable

fun <T, R> T?.ifNotNullComposable(
    block: @Composable (T) -> R
): (@Composable () -> R)? {
    return if (this != null) {
        @Composable {
            block(this)
        }
    } else null
}

fun <R> Boolean.ifComposable(
    block: @Composable () -> R
): (@Composable () -> R)? {
    return if (this) {
        @Composable {
            block()
        }
    } else null
}

fun <T, R> List<T>.ifNotEmptyComposable(
    block: @Composable (List<T>) -> R
): (@Composable () -> R)? {
    return if (isNotEmpty()) {
        @Composable {
            block(this)
        }
    } else null
}