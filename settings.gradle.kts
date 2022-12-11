pluginManagement {
    plugins {
        val agpVersion = "7.3.1"
        id("com.android.application") version agpVersion apply false
        id("com.android.library") version agpVersion apply false

        val kotlinVersion = "1.7.20"
        kotlin("android") version kotlinVersion apply false
        kotlin("plugin.parcelize") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        kotlin("gradle-plugin") version kotlinVersion apply false

        val kspVersion = "1.7.20-1.0.7"
        id("com.google.devtools.ksp") version kspVersion apply false
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "OpenCord"
include(":app")
include(":simpleast-compose")
include(":bottom-dialog-compose")
include(":overlapping-panels-compose")
