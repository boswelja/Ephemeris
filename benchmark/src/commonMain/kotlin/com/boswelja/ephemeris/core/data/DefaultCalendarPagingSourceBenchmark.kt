package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.FocusMode
import com.boswelja.ephemeris.core.model.PageSize
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@State(Scope.Benchmark)
class DefaultCalendarPagingSourceBenchmark : CalendarPagingSourceBenchmark() {
    override fun createPagingSource(): CalendarPageSource {
        val startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDayOfWeek = DayOfWeek.SUNDAY
        return DefaultCalendarPageSource(startDate, startDayOfWeek, FocusMode.WEEKDAYS, PageSize.MONTH)
    }
}
