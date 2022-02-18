package com.boswelja.ephemeris.core

import kotlinx.datetime.LocalDate

data class DisplayWeek(
    val days: Set<LocalDate>
) {
    val startDate = days.first()
    val endDate = days.last()
}
