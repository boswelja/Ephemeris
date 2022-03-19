package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.DisplayDate
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
abstract class CalendarPageSourceBenchmark {

    private lateinit var date: LocalDate

    private lateinit var pageSource: CalendarPageSource

    abstract fun createPagingSource(): CalendarPageSource

    @Setup
    fun setUp() {
        pageSource = createPagingSource()
        date = Clock.System.todayAt(TimeZone.currentSystemDefault())
    }

    @Benchmark
    fun loadPageData() {
        pageSource.loadPageData(11) { date, _ -> DisplayDate(date, false) }
    }

    @Benchmark
    fun getPageFor() {
        pageSource.getPageFor(date)
    }
}
