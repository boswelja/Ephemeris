plugins {
    id("com.android.library")
    kotlin("android")
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
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.get()
    }

    testOptions {
        devices {
            create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api31") {
                device = "Pixel 2"
                apiLevel = 31
                systemImageSource = "aosp"
            }
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    api(project(":core"))

    implementation(libs.bundles.compose.lib)
    implementation(libs.accompanist.pager)
    implementation(libs.chrisbanes.snapper)
}

detekt {
    config = files("${rootDir.absolutePath}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
