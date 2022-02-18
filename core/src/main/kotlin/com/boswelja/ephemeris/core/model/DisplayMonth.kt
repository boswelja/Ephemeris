package com.boswelja.ephemeris.core.model

data class DisplayMonth(
    val yearMonth: YearMonth,
    val weeks: Set<DisplayWeek>
)
