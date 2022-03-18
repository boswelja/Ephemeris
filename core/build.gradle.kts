plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
}

kotlin {
    explicitApi()

    // Android targets
    android()

    // JVM targets
    jvm()

    // JS targets
    js {
        nodejs()
        browser()
        binaries.executable()
    }

    // Windows targets
    mingwX64()

    // Linux targets
    linuxX64()

    // Mac targets
    ios()
    macosX64()
    macosArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.datetime)
                api(libs.kotlinx.coroutines.core)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        targetSdk = 32
        minSdk = 23
    }
}

detekt {
    config = files("${rootDir.absolutePath}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
