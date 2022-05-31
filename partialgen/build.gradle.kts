import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":ksp-util"))
    Dependencies.KSP(this)
    implementation(Dependencies.KotlinX.serialization)
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
}
