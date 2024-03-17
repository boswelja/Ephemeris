// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.22" apply false
    id("org.jetbrains.kotlinx.kover") version "0.7.6" apply false
    id("org.jetbrains.compose") version "1.6.1" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "io.github.boswelja.ephemeris"
version = findProperty("version") ?: "0.1.0"

nexusPublishing {
    repositories {
        sonatype {
            val ossrhUsername: String? by project
            val ossrhPassword: String? by project
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl
                .set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}

tasks.register<Copy>("detektCollateReports") {
    // Set up task
    dependsOn(
        "core:detekt"
    )
    from(
        rootDir.resolve("core/build/reports/detekt/")
    )
    include("detekt.sarif")

    // Delete any existing contents
    buildDir.resolve("reports/detekt/").deleteRecursively()

    // Set up copy
    destinationDir = buildDir.resolve("reports/detekt/")
    rename {
        val totalCount = destinationDir.list()?.count()
        "$totalCount-$it"
    }
}
