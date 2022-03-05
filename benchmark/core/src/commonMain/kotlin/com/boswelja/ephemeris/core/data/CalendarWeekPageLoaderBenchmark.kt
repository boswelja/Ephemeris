package com.boswelja.ephemeris.core.data

import kotlinx.datetime.DayOfWeek

class CalendarWeekPageLoaderBenchmark : CalendarPageLoaderBenchmark() {
    override fun createPagingSource(): CalendarPageLoader {
        return CalendarWeekPageLoader(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = WeekdayFocusMode
        )
    }
}
