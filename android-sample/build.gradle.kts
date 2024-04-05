plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.boswelja.ephemeris.sample"
    compileSdk = SdkVersions.targetSdk

    defaultConfig {
        applicationId = "com.boswelja.ephemeris.sample"
        minSdk = SdkVersions.minSdk
        targetSdk = SdkVersions.targetSdk
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(project(":core"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)

    implementation(compose.material3)
}
