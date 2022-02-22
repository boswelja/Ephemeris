package com.boswelja.ephemeris.core.data

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Mode
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
abstract class CalendarPageCacherBenchmark {

    lateinit var pageSource: CalendarPageCacher

    abstract fun createPagingSource(): CalendarPageCacher

    @Setup
    fun setUp() {
        pageSource = createPagingSource()
    }

    @Benchmark
    fun consecutivePageLoads() {
        repeat(10) {
            pageSource.loadPage(11)
        }
    }

    @Benchmark
    fun consecutiveMonthLoads() {
        repeat(10) {
            pageSource.monthFor(11)
        }
    }

    @Benchmark
    fun iterativePageLoads() {
        var i: Long = 1
        repeat(10) {
            pageSource.loadPage(i++)
        }
    }

    @Benchmark
    fun iterativeMonthLoads() {
        var i: Long = 1
        repeat(10) {
            pageSource.monthFor(i++)
        }
    }
}
