plugins {
    kotlin("android")
    id("com.ephemeris.library.android")
    id("com.ephemeris.publish.maven")
    id("com.ephemeris.quality")
}

android {
    namespace = "com.boswelja.ephemeris.views"
    buildFeatures.viewBinding = true

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api(project(":core"))
    api(libs.kotlinx.coroutines.core)
    api(libs.androidx.recyclerview)

    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.junit)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            artifactId = "android-views"

            pom {
                name.set("android-views")
                description.set("The flexible, multiplatform calendar library!")
                url.set("https://github.com/boswelja/Ephemeris/tree/main/android/views")
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
                    developer {
                        id.set("Iannnr")
                        name.set("Ian Roberts")
                        url.set("https://github.com/Iannnr")
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

tasks
    .matching { it is org.jetbrains.kotlin.gradle.tasks.KotlinCompile && !it.name.contains("test", ignoreCase = true) }
    .configureEach {
        val kotlinCompile = this as org.jetbrains.kotlin.gradle.tasks.KotlinCompile
        if ("-Xexplicit-api=strict" !in kotlinCompile.kotlinOptions.freeCompilerArgs) {
            kotlinCompile.kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
        }
    }
