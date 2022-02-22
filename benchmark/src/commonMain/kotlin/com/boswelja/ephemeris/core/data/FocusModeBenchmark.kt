package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.plus
import com.boswelja.ephemeris.core.model.yearMonth
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
abstract class FocusModeBenchmark {

    private lateinit var focusMode: FocusMode
    private lateinit var date: LocalDate
    private lateinit var sameYearMonth: YearMonth
    private lateinit var differentYearMonth: YearMonth

    abstract fun createFocusMode(): FocusMode

    @Setup
    fun setUp() {
        focusMode = createFocusMode()
        date = Clock.System.todayAt(TimeZone.currentSystemDefault())
        sameYearMonth = date.yearMonth
        differentYearMonth = sameYearMonth.plus(100)
    }

    @Benchmark
    fun focusWithSameMonth() {
        focusMode(date, sameYearMonth)
    }

    @Benchmark
    fun focusWithDifferentMonth() {
        focusMode(date, differentYearMonth)
    }
}
