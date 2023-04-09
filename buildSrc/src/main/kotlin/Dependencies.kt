import org.gradle.kotlin.dsl.DependencyHandlerScope

@Suppress("MemberVisibilityCanBePrivate")
sealed class Dependencies {
    object Ktor : Dependencies() {
        const val version = "2.2.1"

        const val ktorClientCore = "io.ktor:ktor-client-core:$version"
        const val ktorClientOkHttp = "io.ktor:ktor-client-okhttp:$version"
        const val ktorClientWebsockets = "io.ktor:ktor-client-websockets:$version"
        const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val ktorSerializationJson = "io.ktor:ktor-serialization-kotlinx-json:$version"
        const val ktorClientLogging = "io.ktor:ktor-client-logging:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(ktorClientCore)
                implementation(ktorClientOkHttp)
                implementation(ktorClientWebsockets)
                implementation(ktorClientContentNegotiation)
                implementation(ktorSerializationJson)
                implementation(ktorClientLogging)
            }
        }
    }

    object KotlinX : Dependencies() {
        const val datetimeVersion = "0.4.0"
        const val serializationVersion = "1.4.1"
        const val immutableVersion = "0.3.5"

        const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion"
        const val immutable = "org.jetbrains.kotlinx:kotlinx-collections-immutable:$immutableVersion"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(datetime)
                implementation(serialization)
                implementation(immutable)
            }
        }
    }

    object AndroidxCore : Dependencies() {
        const val version = "1.9.0"

        const val core = "androidx.core:core:$version"
        const val coreKtx = "androidx.core:core-ktx:$version"
        const val coreSplashScreen = "androidx.core:core-splashscreen:1.0.0"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(core)
                implementation(coreKtx)
                implementation(coreSplashScreen)
            }
        }
    }

    object AndroidxPreferences : Dependencies() {
        const val version = "1.2.0"

        const val preference = "androidx.preference:preference:$version"
        const val preferenceKtx = "androidx.preference:preference-ktx:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(preference)
                implementation(preferenceKtx)
            }
        }
    }

    object AndroidxRoom : Dependencies() {
        const val version = "2.5.0"

        const val roomCompiler = "androidx.room:room-compiler:$version"
        const val roomRuntime = "androidx.room:room-runtime:$version"
        const val roomKtx = "androidx.room:room-ktx:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                ksp(roomCompiler)
                implementation(roomRuntime)
                implementation(roomKtx)
            }
        }
    }

    object Compose : Dependencies() {
        const val compilerVersion = "1.4.2"

        const val activity = "androidx.activity:activity-compose:1.6.1"
        const val animation = "androidx.compose.animation:animation:1.3.3"
        const val compiler = "androidx.compose.compiler:compiler:$compilerVersion"
        const val foundation = "androidx.compose.foundation:foundation:1.3.1"
        const val material = "androidx.compose.material:material:1.3.1"
        const val material3 = "androidx.compose.material3:material3:1.1.0-alpha08"
        const val runtime = "androidx.compose.runtime:runtime:1.3.3"
        const val ui = "androidx.compose.ui:ui:1.4.0-rc01"
        const val uiText = "androidx.compose.ui:ui-text:1.3.3"
        const val uiUtil = "androidx.compose.ui:ui-util:1.3.3"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(activity)
                implementation(animation)
                implementation(foundation)
                implementation(ui)
                implementation(uiUtil)
                implementation(uiText)
                implementation(runtime)
                implementation(material)
                implementation(material3)
            }
        }
    }

    object Paging : Dependencies() {
        const val runtime = "androidx.paging:paging-runtime:3.1.1"
        const val compose = "androidx.paging:paging-compose:1.0.0-alpha18"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(runtime)
                implementation(compose)
            }
        }
    }

    object Accompanist : Dependencies() {
        const val version = "0.30.1"

        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val placeholder = "com.google.accompanist:accompanist-placeholder:$version"
        const val webview = "com.google.accompanist:accompanist-webview:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(systemUiController)
                implementation(placeholder)
                implementation(webview)
            }
        }
    }

    object Shimmer : Dependencies() {
        const val version = "1.0.3"

        const val shimmer = "com.valentinilk.shimmer:compose-shimmer:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(shimmer)
            }
        }
    }

    object Material : Dependencies() {
        const val version = "1.7.0"

        const val material = "com.google.android.material:material:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(material)
            }
        }
    }

    object Coil : Dependencies() {
        const val version = "2.2.2"

        const val coilBase = "io.coil-kt:coil:$version"
        const val coilCompose = "io.coil-kt:coil-compose:$version"
        const val coilGif = "io.coil-kt:coil-gif:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(coilBase)
                implementation(coilCompose)
                implementation(coilGif)
            }
        }
    }

    object AndroidxMedia3 : Dependencies() {
        const val version = "1.0.0-beta03"

        const val media3exo = "androidx.media3:media3-exoplayer:$version"
        const val media3exoDash = "androidx.media3:media3-exoplayer-dash:$version"
        const val media3Ui = "androidx.media3:media3-ui:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(media3exo)
                implementation(media3exoDash)
                implementation(media3Ui)
            }
        }
    }

    object Koin : Dependencies() {
        // DO NOT UPGRADE
        // it ships with androidx.navigation for no fucking reason
        const val version = "3.2.0"

        const val koin = "io.insert-koin:koin-android:$version"
        const val koinCompose = "io.insert-koin:koin-androidx-compose:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(koin)
                implementation(koinCompose)
            }
        }
    }

    object HCaptcha : Dependencies() {
        const val version = "3.3.6"

        const val hCaptchaSdk = "com.github.hcaptcha:hcaptcha-android-sdk:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(hCaptchaSdk)
            }
        }
    }

    object Partials : Dependencies() {
        const val version = "1.1.0"

        const val partial = "io.github.materiiapps:partial:$version"
        const val partialKsp = "io.github.materiiapps:partial-ksp:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(partial)
                ksp(partialKsp)
            }
        }
    }

    object EnumUtil : Dependencies() {
        const val version = "1.0.0"

        const val enumutil = "io.github.materiiapps:enumutil:$version"
        const val enumutilKsp = "io.github.materiiapps:enumutil-ksp:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(enumutil)
                ksp(enumutilKsp)
            }
        }
    }

    object ReimaginedNav : Dependencies() {
        const val version = "1.3.1"

        const val reimaginedNavigation = "dev.olshevski.navigation:reimagined:$version"

        override fun invoke(scope: DependencyHandlerScope) {
            scope {
                implementation(reimaginedNavigation)
            }
        }
    }

    // TODO: use context receivers when they are enabled by default
    // cannot enable them for build.gradle.kts
    abstract operator fun invoke(scope: DependencyHandlerScope)

    protected fun DependencyHandlerScope.implementation(dependencyNotation: String) {
        "implementation"(dependencyNotation)
    }

    protected fun DependencyHandlerScope.ksp(dependencyNotation: String) {
        "ksp"(dependencyNotation)
    }
}
