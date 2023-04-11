@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.xinto.opencord"
        namespace = "com.xinto.opencord"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"

        buildConfigField("int", "DISCORD_VERSION_CODE", "126021")
        buildConfigField("String", "DISCORD_VERSION_NAME", "\"126.21 - Stable\"")
        buildConfigField("String", "URL_BASE", "\"https://discord.com\"")
        buildConfigField("String", "URL_API", "\"https://discord.com/api/v9\"")
        buildConfigField("String", "URL_CDN", "\"https://cdn.discordapp.com\"")
        buildConfigField("String", "CAPTCHA_KEY", "\"f5561ba9-8f1e-40ca-9b5b-a0b3f719ef34\"")
        buildConfigField(
            "String",
            "URL_GATEWAY",
            "\"wss://gateway.discord.gg/?encoding=json&v=9&compress=zlib-stream\"",
        )

        val languages = project.projectDir.resolve("src/main/res")
            .listFiles { f -> f.isDirectory }
            ?.mapNotNull { it.name.split("values-").getOrNull(1) }
            ?: error("could not get res values folders")

        // Limit 3rd party strings to just the languages we support
        resourceConfigurations += languages
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        // Use java.time.* on Android <= 8
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xcontext-receivers",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${buildDir.resolve("report").absolutePath}",
        )
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
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

    lint {
        disable += "MissingTranslation"
        disable += "ExtraTranslation"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation(project(":simpleast-compose"))

    // Use java.time.* on Android <= 8
    // https://developer.android.com/studio/write/java8-support#library-desugaring-versions
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation(libs.bundles.androidx.core)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.room)
    implementation(libs.bundles.androidx.paging)
    implementation(libs.bundles.androidx.media3)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.materii)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.ktor)
    implementation(libs.koin.compose)
    implementation(libs.navreimagined)
    implementation(libs.hcaptcha)
    implementation(libs.shimmer)

    ksp(libs.materii.partial.ksp)
    ksp(libs.materii.enumutil.ksp)
}
