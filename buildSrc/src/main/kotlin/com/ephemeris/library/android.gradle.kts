package com.ephemeris.library

plugins {
    id("com.android.library")
    id("org.jetbrains.dokka")
}

val isMultiplatform = plugins.hasPlugin("kotlin-multiplatform")

android {
    compileSdk = 32

    defaultConfig {
        targetSdk = 32
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    if (isMultiplatform) {
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
