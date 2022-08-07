plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.boswelja.ephemeris.sample"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.boswelja.ephemeris.sample"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.0")

    implementation(project(":android-compose"))
    implementation(project(":android-views"))

    implementation(libs.bundles.androidx.foundation)
    implementation(libs.bundles.androidx.navigation.views)
    implementation(libs.bundles.compose.mobile)
    implementation(libs.androidx.fragment)
    implementation(libs.binding.delegate)
    implementation(libs.google.material)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
