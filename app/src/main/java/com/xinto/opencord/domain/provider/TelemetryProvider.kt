package com.xinto.opencord.domain.provider

import com.xinto.opencord.BuildConfig
import java.util.*

interface TelemetryProvider {
    val browser: String
    val userAgent: String

    val clientBuildVersion: String
    val clientBuildCode: Int

    val deviceName: String
    val os: String
    val osVersion: String
    val osSdk: String
    val systemLocale: String
    val cpuCores: Int
    val cpuPerformance: Int
    val memoryPerformance: Int
    val accessibility: Boolean
    val accessibilityFeatures: Int

    val deviceAdId: UUID
}

//TODO add device generation
class AnonymousTelemetryProvider: TelemetryProvider {

    override val browser: String =
        "Discord Android"
    override val userAgent: String =
        "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}"

    override val clientBuildVersion: String =
        BuildConfig.DISCORD_VERSION_NAME
    override val clientBuildCode: Int =
        BuildConfig.DISCORD_VERSION_CODE

    override val deviceName: String =
        "Pixel, oriole"
    override val os: String =
        "Android"
    override val osVersion: String =
        "12"
    override val osSdk: String =
        "32"
    override val systemLocale: String =
        "en-US"
    override val cpuCores: Int =
        4
    override val cpuPerformance: Int =
        (5..40).random()
    override val memoryPerformance: Int =
        (100_000..800_000).random()
    override val accessibility:Boolean =
        false
    override val accessibilityFeatures: Int =
        128

    override val deviceAdId: UUID =
        UUID.randomUUID()


}