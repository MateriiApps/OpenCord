plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version "1.6.20-1.0.5"
}

android {
    compileSdk = 32

    flavorDimensions.add("api")

    defaultConfig {
        applicationId = "com.xinto.opencord"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "0.0.1"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("int", "DISCORD_VERSION_CODE", "89108")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    productFlavors {
        create("discord") {
            dimension = "api"
            
            isDefault = true

            buildConfigField("String", "URL_API", "\"https://discord.com/api/v9\"")
            buildConfigField("String", "URL_CDN", "\"https://cdn.discordapp.com\"")
            buildConfigField("String", "URL_GATEWAY", "\"wss://gateway.discord.gg/?v=9&encoding=json&compress=zlib-stream\"")
        }

        create("fosscord") {
            dimension = "api"

            applicationIdSuffix = ".fosscord"
            versionNameSuffix = "-fosscord"

            buildConfigField("String", "URL_API", "\"https://api.fosscord.com/api/v9\"")
            buildConfigField("String", "URL_CDN", "\"https://cdn.fosscord.com\"")
            buildConfigField("String", "URL_GATEWAY", "\"wss://gateway.fosscord.com/?v=9&encoding=json&compress=zlib-stream\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs +
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi" +
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi" +
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi" +
                "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api" +
                "-Xcontext-receivers"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        applicationVariants.all {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }

    kotlin {

    }
}

dependencies {
    implementation(project(":bottom-dialog-compose"))
    implementation(project(":overlapping-panels-compose"))
    implementation(project(":simpleast-compose"))
    implementation(project(":partialgen"))
    ksp(project(":partialgen"))

    Dependencies.Koin(this)
    Dependencies.Ktor(this)
    Dependencies.KotlinXDatetime(this)
    Dependencies.HCaptcha(this)
    Dependencies.AndroidxCore(this)
    Dependencies.AndroidxPreferences(this)
    Dependencies.Material(this)
    Dependencies.Compose(this)
    Dependencies.Accompanist(this)
    Dependencies.Coil(this)
    Dependencies.ExoPlayer(this)
}