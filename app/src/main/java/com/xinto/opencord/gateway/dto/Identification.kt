package com.xinto.opencord.gateway.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identification(
    @SerialName("token")
    val token: String,

    @SerialName("capabilities")
    val capabilities: Int,

    @SerialName("largeThreshold")
    val largeThreshold: Int,

    @SerialName("compress")
    val compress: Boolean,

    @SerialName("properties")
    val properties: IdentificationProperties,

//    @SerialName("presence")
//    val presence: IdentificationPresence,

    @SerialName("clientState")
    val clientState: IdentificationClientState,
)

@Serializable
data class IdentificationClientState(
    @SerialName("guildHashes")
    val guildHashes: Map<String, String>,

    @SerialName("highestLastMessageId")
    val highestLastMessageId: Int,

    @SerialName("readStateVersion")
    val readStateVersion: Int,

    @SerialName("useruserGuildSettingsVersion")
    val userGuildSettingsVersion: Int,
)

@Serializable
data class IdentificationPresence(
    @SerialName("status")
    val status: String,

    @SerialName("since")
    val since: Int,

    @SerialName("activities")
    val activities: List<String>,

    @SerialName("afk")
    val afk: Boolean,
)

@Serializable
data class IdentificationProperties(
    @SerialName("browser")
    val browser: String,

    @SerialName("browser_user_agent")
    val browserUserAgent: String,

    @SerialName("client_build_number")
    val clientBuildNumber: Int,

    @SerialName("client_version")
    val clientVersion: String,

    @SerialName("device")
    val device: String,

    @SerialName("os")
    val os: String,

    @SerialName("os_sdk_version")
    val osSdkVersion: String,

    @SerialName("os_version")
    val osVersion: String,

    @SerialName("system_locale")
    val systemLocale: String,
)
