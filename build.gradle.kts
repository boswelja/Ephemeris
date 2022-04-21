import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlinx.kover") version "0.5.0" apply false
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
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

// Enable SARIF reports for Detekt
val detektMergeReport by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    val outFile = rootProject.buildDir.resolve("reports/detekt.sarif")
    output.set(outFile)
    doLast {
        if (outFile.exists()) {
            println("Wrote Detekt SARIF report to ${outFile.path}")
        } else {
            println("Failed to write SARIF report")
        }
    }
}

subprojects {
    plugins.withType(io.gitlab.arturbosch.detekt.DetektPlugin::class) {
        tasks.register<Detekt>("detektReport") {
            reports {
                sarif.required.set(true)
                xml.required.set(false)
                txt.required.set(false)
                html.required.set(false)
            }
            finalizedBy(detektMergeReport)

            detektMergeReport.configure {
                input.from(this@register.sarifReportFile)
            }
        }
    }
}
