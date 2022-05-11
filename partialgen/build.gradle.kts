plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":ksp-util"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.20-1.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}
