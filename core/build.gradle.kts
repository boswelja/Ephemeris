import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.kover")
    id("com.android.library")
    id("com.ephemeris.publish.maven")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
}

version = findProperty("version")?.let {
    if (it == Project.DEFAULT_VERSION) null
    else it
} ?: "0.1.0"

kotlin {
    explicitApi()

    // Android targets
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

detekt {
    config.setFrom(
        "${rootDir.absolutePath}/config/detekt/detekt-base.yml",
    )
    basePath = rootDir.absolutePath
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    reports.sarif.required.set(true)
}

kover {
    verify {
        rule {
            name = "Code line coverage"
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
