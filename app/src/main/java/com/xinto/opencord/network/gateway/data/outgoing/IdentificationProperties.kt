package com.xinto.opencord.network.gateway.data.outgoing

data class IdentificationProperties(
    val browser: String,
    val browser_user_agent: String,
    val client_build_number: Int,
    val client_version: String,
    val device: String,
    val os: String,
    val os_sdk_version: String,
    val os_version: String,
    val system_locale: String,
)
