package com.xinto.opencord.util

import androidx.compose.runtime.Composable

inline fun <reified T> T.letComposable(
    crossinline block: @Composable (T) -> Unit
): @Composable () -> Unit {
    return let {
        @Composable {
            block(it)
        }
    }
}