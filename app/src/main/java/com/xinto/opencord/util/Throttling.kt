package com.xinto.opencord.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Constructs a function that executes [destinationFunction] only once per [skipMs].
 */
inline fun throttle(
    skipMs: Long,
    coroutineScope: CoroutineScope,
    crossinline destinationFunction: suspend () -> Unit
): () -> Unit {
    var throttleJob: Job? = null
    return {
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction()
                delay(skipMs)
            }
        }
    }
}

inline fun <T> throttle(
    skipMs: Long,
    coroutineScope: CoroutineScope,
    crossinline destinationFunction: suspend (T) -> Unit
): (T) -> Unit {
    var throttleJob: Job? = null
    return { arg1 ->
        if (throttleJob?.isCompleted != false) {
            throttleJob = coroutineScope.launch {
                destinationFunction(arg1)
                delay(skipMs)
            }
        }
    }
}
