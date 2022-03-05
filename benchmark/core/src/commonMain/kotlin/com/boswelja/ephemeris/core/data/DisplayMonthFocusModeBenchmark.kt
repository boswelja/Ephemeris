package com.boswelja.ephemeris.core.data

import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

@State(Scope.Benchmark)
class DisplayMonthFocusModeBenchmark : FocusModeBenchmark() {
    override fun createFocusMode(): FocusMode {
        return DisplayMonthFocusMode
    }
}
