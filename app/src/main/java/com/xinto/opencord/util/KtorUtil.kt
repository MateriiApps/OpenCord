package com.xinto.opencord.util

import io.ktor.http.*

fun queryParameters(internalSize: Int = 4, block: ParametersBuilder.() -> Unit): String {
    val builder = ParametersBuilder(internalSize)
    block(builder)

    return if (builder.isEmpty()) {
        ""
    } else {
        "?${builder.build().formUrlEncode()}"
    }
}
