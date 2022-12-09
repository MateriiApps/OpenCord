package com.xinto.opencord.provider

import com.xinto.opencord.gateway.dto.IdentificationProperties
import com.xinto.opencord.rest.body.XSuperProperties

interface PropertyProvider {
    val xSuperProperties: XSuperProperties
    val identificationProperties: IdentificationProperties
}

class PropertyProviderImpl(
    telemetryProvider: TelemetryProvider
) : PropertyProvider {
    override val xSuperProperties: XSuperProperties =
        XSuperProperties(
            browser = telemetryProvider.browser,
            userAgent = telemetryProvider.userAgent,
            clientBuildVersion = telemetryProvider.clientBuildVersion,
            clientBuildNumber = telemetryProvider.clientBuildCode,
            deviceName = telemetryProvider.deviceName,
            os = telemetryProvider.os,
            osVersion = telemetryProvider.osVersion,
            osSdkVersion = telemetryProvider.osSdk,
            systemLocale = telemetryProvider.systemLocale,
            cpuCores = telemetryProvider.cpuCores,
            cpuPerformance = telemetryProvider.cpuPerformance,
            memoryPerformance = telemetryProvider.memoryPerformance,
            accessibilitySupport = telemetryProvider.accessibility,
            accessibilityFeatures = telemetryProvider.accessibilityFeatures,
            deviceAdId = telemetryProvider.deviceAdId.toString(),
        )

    override val identificationProperties: IdentificationProperties =
        IdentificationProperties(
            browser = telemetryProvider.browser,
            browserUserAgent = telemetryProvider.userAgent,
            clientBuildNumber = telemetryProvider.clientBuildCode,
            clientVersion = telemetryProvider.clientBuildVersion,
            device = telemetryProvider.deviceName,
            os = telemetryProvider.os,
            osVersion = telemetryProvider.osVersion,
            osSdkVersion = telemetryProvider.osSdk,
            systemLocale = telemetryProvider.systemLocale,
        )
}
