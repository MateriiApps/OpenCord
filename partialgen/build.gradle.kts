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
