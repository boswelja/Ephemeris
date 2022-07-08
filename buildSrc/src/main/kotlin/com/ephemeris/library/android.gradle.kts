package com.ephemeris.library

plugins {
    id("com.android.library")
}

val isMultiplatform = plugins.hasPlugin("kotlin-multiplatform")

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    if (isMultiplatform) {
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
