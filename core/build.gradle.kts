plugins {
    id("com.android.library")
    id("kotlin-android")
    id("io.gitlab.arturbosch.detekt")
}

android {
    compileSdk = 32

    defaultConfig {
        targetSdk = 32
        minSdk = 23
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
}

detekt {
    config = files("../config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
