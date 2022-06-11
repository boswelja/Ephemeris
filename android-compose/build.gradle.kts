import java.net.URL
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    kotlin("android")
    id("com.ephemeris.library.android")
    id("com.ephemeris.publish.maven")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.boswelja.ephemeris.compose"
    buildFeatures.compose = true

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.get()
    }
}

dependencies {
    api(project(":core"))
    api(libs.bundles.compose.lib)
    implementation(libs.compose.snapper)
}

detekt {
    config = files(
        "${rootDir.absolutePath}/config/detekt/detekt-base.yml",
        "${rootDir.absolutePath}/config/detekt/detekt-compose.yml"
    )
    basePath = rootDir.absolutePath
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    reports.sarif.required.set(true)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            pom {
                name.set("android-compose")
                description.set("The flexible, multiplatform calendar library!")
                url.set("https://github.com/boswelja/Ephemeris/tree/main/android/compose")
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

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks
    .matching { it is org.jetbrains.kotlin.gradle.tasks.KotlinCompile && !it.name.contains("test", ignoreCase = true) }
    .configureEach {
        val kotlinCompile = this as org.jetbrains.kotlin.gradle.tasks.KotlinCompile
        if ("-Xexplicit-api=strict" !in kotlinCompile.kotlinOptions.freeCompilerArgs) {
            kotlinCompile.kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
        }
    }

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets {
        named("main") {
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))

                remoteUrl.set(
                    URL("https://github.com/boswelja/Ephemeris/blob/main/android-compose/src/main/kotlin")
                )
            }
        }
    }
}
