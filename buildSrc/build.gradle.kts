plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("com.android.library:com.android.library.gradle.plugin:7.3.0-alpha09")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.20")
}
