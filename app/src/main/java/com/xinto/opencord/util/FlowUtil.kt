package com.xinto.opencord.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectIn(scope: CoroutineScope, collector: FlowCollector<T>): Job {
    return scope.launch {
        this@collectIn.collect(collector)
    }
}
