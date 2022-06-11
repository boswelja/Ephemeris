package com.boswelja.ephemeris.sample.main

sealed class MainItem

data class Header(
    val text: String
) : MainItem()

data class Sample(
    val navAction: Int,
    val text: String
) : MainItem()
