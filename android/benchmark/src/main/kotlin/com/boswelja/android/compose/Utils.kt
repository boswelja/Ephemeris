package com.boswelja.android.compose

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import java.util.concurrent.TimeUnit

object Utils {
    const val PackageName = "com.boswelja.ephemeris.sample"

    fun MacrobenchmarkScope.launchComposeCalendar() {
        pressHome()
        startActivityAndWait()
        device.findObject(By.res(PackageName, "compose_link"))
            .click()
        device.wait(
            Until.hasObject(By.clazz("$PackageName.compose.ComposeCalendarFragment")),
            TimeUnit.SECONDS.toMillis(5)
        )
    }
}
