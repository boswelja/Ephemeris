package com.boswelja.ephemeris.core.data

import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@State(Scope.Benchmark)
class DefaultCalendarPagingSourceBenchmark : CalendarPagingSourceBenchmark() {
    override fun createPagingSource(): CalendarPagingSource {
        val startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDayOfWeek = DayOfWeek.SUNDAY
        return DefaultCalendarPagingSource(startDate, startDayOfWeek)
    }
}
