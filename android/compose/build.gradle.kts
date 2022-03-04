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

    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha02"
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(project(":core"))
    implementation("androidx.compose.animation:animation:1.2.0-alpha03")
    implementation("androidx.compose.ui:ui:1.2.0-alpha03")
    implementation("androidx.compose.foundation:foundation:1.2.0-alpha03")
    implementation("dev.chrisbanes.snapper:snapper:0.2.0")
    implementation("com.google.accompanist:accompanist-pager:0.24.2-alpha")
}

detekt {
    config = files("${rootDir.absolutePath}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
