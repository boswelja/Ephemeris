package com.boswelja.android.compose

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import com.boswelja.android.compose.Utils.launchComposeCalendar
import com.boswelja.android.views.Utils.PackageName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrollMonthsBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun flingLeft() = benchmarkRule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(FrameTimingMetric()),
        iterations = 3,
        compilationMode = CompilationMode.None(),
        startupMode = StartupMode.COLD,
        setupBlock = {
            launchComposeCalendar()
        }
    ) {
        repeat(3) {
            val calendar = device.findObject(By.desc("calendar"))
            calendar.setGestureMargin(device.displayWidth / 5)
            calendar.fling(Direction.LEFT)
        }
    }

    @Test
    fun flingRight() = benchmarkRule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(FrameTimingMetric()),
        iterations = 3,
        compilationMode = CompilationMode.None(),
        setupBlock = {
            launchComposeCalendar()
        }
    ) {
        repeat(3) {
            val calendar = device.findObject(By.desc("calendar"))
            calendar.setGestureMargin(device.displayWidth / 5)
            calendar.fling(Direction.RIGHT)
        }
    }
}
