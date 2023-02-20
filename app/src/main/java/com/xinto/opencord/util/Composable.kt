package com.xinto.opencord.util

import androidx.compose.runtime.Composable

inline fun <T, R> T?.ifNotNullComposable(
    crossinline block: @Composable (T) -> R
): (@Composable () -> R)? {
    return if (this != null) {
        @Composable {
            block(this)
        }
    } else null
}

inline fun <R> Boolean.ifComposable(
    crossinline block: @Composable () -> R
): (@Composable () -> R)? {
    return if (this) {
        @Composable {
            block()
        }
    } else null
}

inline fun <T, R> List<T>.ifNotEmptyComposable(
    crossinline block: @Composable (List<T>) -> R
): (@Composable () -> R)? {
    return if (isNotEmpty()) {
        @Composable {
            block(this)
        }
    } else null
}
