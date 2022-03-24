package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.TearDown
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.datetime.DayOfWeek

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
class CalendarPageLoaderBenchmark {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var pageLoader: CalendarPageLoader

    private var page = 0

    @Setup
    fun setUp() {
        coroutineScope = CoroutineScope(Dispatchers.Default)
        pageLoader = CalendarPageLoader(
            coroutineScope,
            CalendarMonthPageSource(DayOfWeek.SUNDAY, focusMode = CalendarMonthPageSource.FocusMode.ALL)
        )
    }

    @TearDown
    fun tearDown() {
        coroutineScope.cancel()
    }

    @Benchmark
    fun getPageData_forNewPages() {
        pageLoader.getPageData(page++)
    }

    @Benchmark
    fun getPageData_forSamePage() {
        pageLoader.getPageData(page)
    }
}
