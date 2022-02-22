plugins {
    kotlin("multiplatform")
    kotlin("plugin.allopen") version "1.6.10"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.2"
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.2")
            }
        }
    }
}

benchmark {
    configurations {
        getByName("main") {
            iterations = 5
            warmups = 3
            iterationTime = 1
            outputTimeUnit = "ns"
        }
    }
    targets {
        register("jvm")
    }
}
