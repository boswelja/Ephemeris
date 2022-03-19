package com.boswelja.ephemeris.core.data

import kotlinx.datetime.DayOfWeek

class CalendarWeekPageSourceBenchmark : CalendarPageSourceBenchmark() {
    override fun createPagingSource(): CalendarPageSource {
        return CalendarWeekPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY
        )
    }
}
