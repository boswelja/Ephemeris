package com.ephemeris.publish

plugins {
    id("signing")
    id("maven-publish")
}

val isMultiplatform = plugins.hasPlugin("kotlin-multiplatform")

group = "io.github.boswelja.ephemeris"
val version: String? by project

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

if (isMultiplatform) {
    tasks {
        create<Jar>("javadocJar") {
            archiveClassifier.set("javadoc")
        }
    }
}

publishing {
    publications {
        repositories {
            maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
                val ossrhUsername: String? by project
                val ossrhPassword: String? by project
                name = "sonatype"
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }
}
