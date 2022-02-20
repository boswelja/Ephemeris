package com.boswelja.ephemeris.core.data

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
abstract class CalendarPagingSourceBenchmark {

    lateinit var pagingSource: CalendarPageSource

    abstract fun createPagingSource(): CalendarPageSource

    @Setup
    fun setUp() {
        pagingSource = createPagingSource()
    }

    @Benchmark
    fun loadPage() {
        pagingSource.loadPage(11)
    }

    @Benchmark
    fun getMonthForPage() {
        pagingSource.monthFor(11)
    }
}
