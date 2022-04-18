package com.xinto.opencord.rest.dto

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ApiPermissions(val value: Long) {
    constructor(flags: String): this(flags.toLong())

    override fun toString(): String = value.toString()
}