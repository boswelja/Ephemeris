package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.FocusMode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@State(Scope.Benchmark)
class CalendarMonthPageSourceBenchmark : CalendarPagingSourceBenchmark() {
    override fun createPagingSource(): CalendarPageSource {
        val startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDayOfWeek = DayOfWeek.SUNDAY
        return CalendarMonthPageSource(startDate, startDayOfWeek, FocusMode.CURRENT_MONTH)
    }
}
