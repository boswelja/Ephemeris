package com.boswelja.ephemeris.core.data

import kotlinx.datetime.DayOfWeek

class CalendarMonthPageSourceBenchmark : CalendarPageSourceBenchmark() {
    override fun createPagingSource(): CalendarPageSource {
        return CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY
        )
    }
}
