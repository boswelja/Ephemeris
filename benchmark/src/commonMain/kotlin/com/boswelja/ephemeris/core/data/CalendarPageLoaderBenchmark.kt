package com.boswelja.ephemeris.core.data

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
abstract class CalendarPageLoaderBenchmark {

    private lateinit var pageSource: CalendarPageLoader

    abstract fun createPagingSource(): CalendarPageLoader

    @Setup
    fun setUp() {
        pageSource = createPagingSource()
    }

    @Benchmark
    fun loadPage() {
        pageSource.loadPage(11)
    }

    @Benchmark
    fun monthFor() {
        pageSource.monthFor(11)
    }
}
