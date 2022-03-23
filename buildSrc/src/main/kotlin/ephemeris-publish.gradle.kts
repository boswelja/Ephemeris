import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing

plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

val version: String? by project

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}

tasks {
    create<Jar>("javadocJar") {
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.get().outputDirectory)
    }
}

publishing {
    publications.withType<MavenPublication> {
        artifact(tasks["javadocJar"])

        pom {
            // If the project has a parent, use that as part of the name
            val projectName = project.parent?.let {
                "${it.name}-${project.name}"
            } ?: project.name
            name.set(projectName)
            description.set(project.description)
            url.set("https://github.com/boswelja/Ephemeris")
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
