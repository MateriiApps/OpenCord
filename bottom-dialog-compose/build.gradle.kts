plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = false
        resValues = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
    }
}

dependencies {
    implementation(Dependencies.Material.material)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.uiUtil)
}
