package com.xinto.opencord.provider

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
class AnonymousTelemetryProvider : TelemetryProvider {
    private val device = AnonymousDevice.devices.random()

    override val browser = "Discord Android"
    override val userAgent = "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}"

    override val clientBuildVersion = BuildConfig.DISCORD_VERSION_NAME
    override val clientBuildCode = BuildConfig.DISCORD_VERSION_CODE

    override val deviceName = device.name
    override val os = device.os
    override val osVersion = device.osVersion
    override val osSdk = device.osSdk
    override val systemLocale = device.systemLocale
    override val cpuCores = device.cpuCores
    override val cpuPerformance = device.cpuPerformance
    override val memoryPerformance = device.memoryPerformance
    override val accessibility = false
    override val accessibilityFeatures = 128

    override val deviceAdId: UUID = UUID.randomUUID()
}

data class AnonymousDevice(
    val name: String,
    val os: String,
    val osVersion: String,
    val osSdk: String,
    val systemLocale: String,
    val cpuCores: Int,
    val cpuPerformance: Int,
    val memoryPerformance: Int,
) {
    companion object {
        val devices = listOf(
            buildAnonymousDevice(
                name = "Pixel, coral",
                cpuCores = 8,
            ),
            buildAnonymousDevice(
                name = "Pixel, redfin",
                cpuCores = 8,
            ),
            buildAnonymousDevice(
                name = "Pixel, oriole",
                cpuCores = 8,
            ),
            buildAnonymousDevice(
                name = "Pixel, raven",
                cpuCores = 8,
            ),
        )

        private fun buildAnonymousDevice(name: String, cpuCores: Int): AnonymousDevice {
            return AnonymousDevice(
                name = name,
                os = "Android",
                osVersion = "12",
                osSdk = "32",
                systemLocale = "en-US",
                cpuCores = cpuCores,
                cpuPerformance = cpuCores * (7..10).random(),
                memoryPerformance = cpuCores * (50_000..100_000).random(),
            )
        }
    }
}
