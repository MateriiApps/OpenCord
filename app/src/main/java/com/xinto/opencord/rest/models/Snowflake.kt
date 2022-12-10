package com.xinto.opencord.rest.models

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ApiSnowflake(val value: Long) {
    constructor(value: String) : this(value.toLong())

    override fun toString(): String = value.toString()
}
