package com.xinto.opencord.rest.models.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLoginUserSettings(
    @SerialName("locale")
    val locale: String,

    @SerialName("theme")
    val theme: String,
)
