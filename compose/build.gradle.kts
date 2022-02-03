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

    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha02"
    }
}

dependencies {
    api(project(":core"))
    implementation("androidx.compose.animation:animation:1.2.0-alpha02")
    implementation("androidx.compose.ui:ui:1.2.0-alpha02")
    implementation("androidx.compose.foundation:foundation:1.2.0-alpha02")
    implementation("dev.chrisbanes.snapper:snapper:0.1.2")
}

detekt {
    config = files("../config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
