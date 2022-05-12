plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":ksp-util"))
    Dependencies.KSP(this)
}
