package com.ephemeris.library

plugins {
    id("com.android.library")
}

val isMultiplatform = plugins.hasPlugin("kotlin-multiplatform")

android {
    compileSdk = 32

    defaultConfig {
        targetSdk = 32
        minSdk = 23
    }
    if (isMultiplatform) {
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}
