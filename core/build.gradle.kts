import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    id("org.jetbrains.kotlinx.kover")
    id("com.ephemeris.publish.maven")
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
}

version = findProperty("version")?.let {
    if (it == Project.DEFAULT_VERSION) null
    else it
} ?: "0.1.0"

kotlin {
    jvmToolchain(17)

    explicitApi()

    // Android targets
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.datetime)
                api(libs.kotlinx.coroutines.core)

                implementation(compose.animation)
                implementation(compose.foundation)
                implementation(compose.ui)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "com.boswelja.ephemeris.core"

    compileSdk = SdkVersions.targetSdk

    defaultConfig {
        minSdk = SdkVersions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt.yml")
    basePath = rootDir.absolutePath
}

koverReport {
    verify {
        rule("Code line coverage") {
            bound {
                minValue = 90
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        artifact(tasks["javadocJar"])

        pom {
            name.set("core")
            description.set("The flexible, multiplatform calendar library!")
            url.set("https://github.com/boswelja/Ephemeris/tree/main/core")
            licenses {
                license {
                    name.set("GPLv3")
                    url.set("https://github.com/boswelja/Ephemeris/blob/main/LICENSE")
                }
            }
            developers {
                developer {
                    id.set("boswelja")
                    name.set("Jack Boswell")
                    email.set("boswelja@outlook.com")
                    url.set("https://github.com/boswelja")
                }
            }
            scm {
                connection.set("scm:git:github.com/boswelja/Ephemeris.git")
                developerConnection.set("scm:git:ssh://github.com/boswelja/Ephemeris.git")
                url.set("https://github.com/boswelja/Ephemeris")
            }
        }
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(file("src/commonMain/kotlin"))

            remoteUrl.set(
                URL(
                    "https://github.com/boswelja/Ephemeris/blob/main/core/src/commonMain/kotlin"
                )
            )
        }
    }
}
