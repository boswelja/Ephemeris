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

    buildFeatures.viewBinding = true
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(project(":core"))

    implementation("androidx.viewpager2:viewpager2:1.0.0")
    api("androidx.recyclerview:recyclerview:1.2.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

detekt {
    config = files("${rootDir.absolutePath}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
