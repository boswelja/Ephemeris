pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Ephemeris"
include(
    ":core",
    ":compose",
    ":benchmark:core",
    ":benchmark:android",
    ":android:views",
    ":android:sample",
)
