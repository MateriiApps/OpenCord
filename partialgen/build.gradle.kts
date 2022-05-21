plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":ksp-util"))
    Dependencies.KSP(this)
    implementation(Dependencies.KotlinX.serialization)
}
