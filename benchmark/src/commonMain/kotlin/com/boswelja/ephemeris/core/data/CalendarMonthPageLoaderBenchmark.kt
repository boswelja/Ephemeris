package com.boswelja.ephemeris.core.data

import kotlinx.datetime.DayOfWeek

class CalendarMonthPageLoaderBenchmark : CalendarPageLoaderBenchmark() {
    override fun createPagingSource(): CalendarPageLoader {
        return CalendarMonthPageLoader(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = WeekdayFocusMode
        )
    }
}
