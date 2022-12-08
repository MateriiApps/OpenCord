package com.xinto.opencord.rest.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwoFactorBody(
    @SerialName("code")
    val code: String,

    @SerialName("ticket")
    val ticket: String,
)
