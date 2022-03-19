package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.DisplayDate
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
abstract class CalendarPageSourceBenchmark {

    private lateinit var pageSource: CalendarPageSource

    abstract fun createPagingSource(): CalendarPageSource

    @Setup
    fun setUp() {
        pageSource = createPagingSource()
    }

    @Benchmark
    fun loadPage() {
        pageSource.loadPage(11) { date, _ -> DisplayDate(date, false) }
    }

    @Benchmark
    fun monthFor() {
        pageSource.monthFor(11)
    }
}
