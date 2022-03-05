plugins {
    kotlin("multiplatform")
    kotlin("plugin.allopen")
    id("org.jetbrains.kotlinx.benchmark")
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
                implementation(libs.kotlinx.benchmark)
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
