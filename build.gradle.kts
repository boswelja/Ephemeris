// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.3.0-alpha07" apply false
    id("com.android.library") version "7.3.0-alpha07" apply false
    id("com.android.test") version "7.3.0-alpha07" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlinx.benchmark") version "0.4.2" apply false
    kotlin("plugin.allopen") version "1.6.10" apply false
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
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
