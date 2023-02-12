pluginManagement {
    plugins {
        val agpVersion = "7.4.1"
        id("com.android.application") version agpVersion apply false
        id("com.android.library") version agpVersion apply false

        val kotlinVersion = "1.8.10"
        kotlin("android") version kotlinVersion apply false
        kotlin("plugin.parcelize") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        kotlin("gradle-plugin") version kotlinVersion apply false

        val kspVersion = "1.8.10-1.0.9"
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
