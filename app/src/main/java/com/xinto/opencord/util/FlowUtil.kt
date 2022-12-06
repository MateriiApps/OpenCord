package com.xinto.opencord.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.collectIn(scope: CoroutineScope, action: suspend (T) -> Unit): Job {
    return this.onEach(action).launchIn(scope)
}
