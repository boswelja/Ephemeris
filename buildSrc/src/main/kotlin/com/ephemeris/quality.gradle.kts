package com.ephemeris

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    config = files("${rootDir.absolutePath}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
