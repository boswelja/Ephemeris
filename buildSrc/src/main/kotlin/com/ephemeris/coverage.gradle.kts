package com.ephemeris

plugins {
    id("org.jetbrains.kotlinx.kover")
}

tasks.koverVerify {
    rule {
        name = "Code line coverage"
        bound {
            minValue = 75
        }
    }
}
