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

    compileOptions.isCoreLibraryDesugaringEnabled = true
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    testImplementation("junit:junit:4.13.2")
}

detekt {
    config = files("../config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
