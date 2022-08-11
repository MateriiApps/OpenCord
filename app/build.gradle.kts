plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

android {
    compileSdk = 33

    flavorDimensions.add("api")

    defaultConfig {
        applicationId = "com.xinto.opencord"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("int", "DISCORD_VERSION_CODE", "124012")
        buildConfigField("String", "DISCORD_VERSION_NAME", "\"124.12 - Stable\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    productFlavors {
        create("discord") {
            dimension = "api"

            isDefault = true

            buildConfigField("String", "URL_API", "\"https://discord.com/api/v9\"")
            buildConfigField("String", "URL_CDN", "\"https://cdn.discordapp.com\"")
            buildConfigField(
                "String",
                "URL_GATEWAY",
                "\"wss://gateway.discord.gg/?v=9&encoding=json&compress=zlib-stream\""
            )
        }

        create("fosscord") {
            dimension = "api"

            applicationIdSuffix = ".fosscord"
            versionNameSuffix = "-fosscord"

            buildConfigField("String", "URL_API", "\"https://api.fosscord.com/api/v9\"")
            buildConfigField("String", "URL_CDN", "\"https://cdn.fosscord.com\"")
            buildConfigField(
                "String",
                "URL_GATEWAY",
                "\"wss://gateway.fosscord.com/?v=9&encoding=json&compress=zlib-stream\""
            )
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
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }

    androidComponents {
        onVariants(selector().withBuildType("release")) {
            it.packaging.resources.excludes.apply {
                // Debug metadata
                add("/**/*.version")
                add("/kotlin-tooling-metadata.json")
                // Kotlin debugging (https://github.com/Kotlin/kotlinx.coroutines/issues/2274)
                add("/DebugProbesKt.bin")
            }
        }
    }

    packagingOptions {
        resources {
            // Lombok
            excludes += "/AUTHORS"
            excludes += "/latestchanges.html"
            excludes += "/release-timestamp.txt"
            excludes += "/changelog.txt"
            excludes += "/README.md"
            excludes += "/lombok/**"
            excludes += "/**/*.lombok"

            // Reflection symbol list (https://stackoverflow.com/a/41073782/13964629)
            excludes += "/**/*.kotlin_builtins"

            // okhttp3 is used by some lib (no cookies so publicsuffixes.gz can be dropped)
            excludes += "/okhttp3/**"
        }
    }

    sourceSets {
        applicationVariants.all {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(project(":bottom-dialog-compose"))
    implementation(project(":overlapping-panels-compose"))
    implementation(project(":simpleast-compose"))

    implementation(project(":partialgen"))
    ksp(project(":partialgen"))

    implementation(project(":enumgetter"))
    ksp(project(":enumgetter"))

    Dependencies.Koin(this)
    Dependencies.Ktor(this)
    Dependencies.KotlinX(this)
    Dependencies.HCaptcha(this)
    Dependencies.AndroidxCore(this)
    Dependencies.AndroidxPreferences(this)
    Dependencies.Material(this)
    Dependencies.Compose(this)
    Dependencies.Accompanist(this)
    Dependencies.Shimmer(this)
    Dependencies.Coil(this)
    Dependencies.ExoPlayer(this)
}
