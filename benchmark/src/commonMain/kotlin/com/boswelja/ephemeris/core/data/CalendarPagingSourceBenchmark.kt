package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.PageSize
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
abstract class CalendarPagingSourceBenchmark {

    lateinit var pagingSource: CalendarPagingSource

    abstract fun createPagingSource(): CalendarPagingSource

    @Setup
    fun setUp() {
        pagingSource = createPagingSource()
    }

    @Benchmark
    fun loadWeekPage() {
        pagingSource.loadPage(11, PageSize.WEEK)
    }

    @Benchmark
    fun loadMonthPage() {
        pagingSource.loadPage(5, PageSize.MONTH)
    }

    @Benchmark
    fun getMonthForWeekPage() {
        pagingSource.monthFor(5, PageSize.WEEK)
    }

    @Benchmark
    fun getMonthForMonthPage() {
        pagingSource.monthFor(11, PageSize.MONTH)
    }
}
