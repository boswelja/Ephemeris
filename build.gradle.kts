// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
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
