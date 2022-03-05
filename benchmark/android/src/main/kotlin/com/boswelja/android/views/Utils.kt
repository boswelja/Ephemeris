package com.boswelja.android.views

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import java.util.concurrent.TimeUnit

object Utils {
    const val PackageName = "com.boswelja.ephemeris.sample"

    fun MacrobenchmarkScope.launchViewsCalendar() {
        pressHome()
        startActivityAndWait()
        device.findObject(By.res(PackageName, "xmlviews_link"))
            .click()
        device.wait(
            Until.hasObject(By.clazz("$PackageName.views.ViewsCalendarFragment")),
            TimeUnit.SECONDS.toMillis(5)
        )
    }
}
