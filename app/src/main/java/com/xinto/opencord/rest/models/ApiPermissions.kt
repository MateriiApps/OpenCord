package com.xinto.opencord.rest.models

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ApiPermissions(val value: Long) {
    constructor(flags: String) : this(flags.toLong())

    override fun toString(): String = value.toString()
}
