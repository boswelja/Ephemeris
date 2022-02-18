package com.boswelja.ephemeris.core

data class DisplayMonth(
    val yearMonth: YearMonth,
    val weeks: Set<DisplayWeek>
)
