plugins {
    kotlin("android")
    id("com.ephemeris.library.android")
    id("com.ephemeris.publish.maven")
    id("com.ephemeris.quality")
}

android {
    namespace = "com.boswelja.ephemeris.views"
    buildFeatures.viewBinding = true
}

dependencies {
    api(project(":core"))

    api(libs.kotlinx.coroutines.core)

    api(libs.androidx.recyclerview)
    implementation(libs.androidx.appcompat)
}
