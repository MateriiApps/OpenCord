package com.xinto.opencord.util

import androidx.compose.runtime.Composable

fun <T, R> T.letComposable(
    block: @Composable (T) -> R
): @Composable () -> R {
    return let {
        @Composable {
            block(it)
        }
    }
}