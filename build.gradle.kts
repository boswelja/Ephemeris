// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlinx.kover") version "0.5.1" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
    id("org.jetbrains.dokka")
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
        "android-views:detekt",
        "android-compose:detekt",
        "core:detekt"
    )
    from(
        rootDir.resolve("android-views/build/reports/detekt/"),
        rootDir.resolve("android-compose/build/reports/detekt/"),
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
