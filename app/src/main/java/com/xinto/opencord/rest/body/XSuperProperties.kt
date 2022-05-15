package com.xinto.opencord.rest.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class XSuperProperties(
    @SerialName("browser")
    val browser: String,

    @SerialName("browser_user_agent")
    val userAgent: String,

    @SerialName("client_build_number")
    val clientBuildNumber: Int,

    @SerialName("client_version")
    val clientBuildVersion: String,

    @SerialName("device")
    val deviceName: String,

    @SerialName("os")
    val os: String,

    @SerialName("os_sdk_version")
    val osSdkVersion: String,

    @SerialName("os_version")
    val osVersion: String,

    @SerialName("system_locale")
    val systemLocale: String,

    @SerialName("client_performance_cpu")
    val cpuPerformance: Int,

    @SerialName("client_performance_memory")
    val memoryPerformance: Int,

    @SerialName("cpu_core_count")
    val cpuCores: Int,

    @SerialName("accessibility_support_enabled")
    val accessibilitySupport: Boolean,

    @SerialName("accessibility_features")
    val accessibilityFeatures: Int,

    @SerialName("device_advertiser_id")
    val deviceAdId: String,
)
