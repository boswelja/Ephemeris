// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.3.0-alpha07" apply false
    id("com.android.library") version "7.3.0-alpha07" apply false
    id("com.android.test") version "7.3.0-alpha07" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlinx.benchmark") version "0.4.2" apply false
    kotlin("plugin.allopen") version "1.6.10" apply false
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
}
