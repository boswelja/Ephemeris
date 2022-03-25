plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.1.1"
    id("com.ephemeris.library.android")
    id("com.ephemeris.publish.maven")
    id("com.ephemeris.quality")
}

kotlin {
    explicitApi()

    // Android targets
    android {
        publishLibraryVariants("release")
    }

    // JVM targets
    jvm()

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
        commonMain {
            dependencies {
                api(project(":core"))
                api(libs.kotlinx.datetime)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.animation)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android.namespace = "com.boswelja.ephemeris.compose"
