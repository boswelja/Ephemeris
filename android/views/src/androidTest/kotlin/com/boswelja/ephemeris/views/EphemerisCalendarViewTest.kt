package com.boswelja.ephemeris.views

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import com.boswelja.ephemeris.views.datebinders.BasicDateBinder
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@LargeTest
class EphemerisCalendarViewTest {

    @Test
    fun onPageSourceChange_heightChanges() {
        // Test height decrease
        launchFragmentInContainer<EphemerisCalendarFragment>().use { scenario ->
            scenario.onFragment {
                it.calendarView.initCalendar(
                    CalendarMonthPageSource(DayOfWeek.SUNDAY),
                    BasicDateBinder()
                )
            }
            Thread.sleep(300)
            var initialHeight = 0
            scenario.onFragment {
                initialHeight = it.calendarView.height
                it.calendarView.pageSource = CalendarWeekPageSource(DayOfWeek.SUNDAY)
            }
            Thread.sleep(300)
            scenario.onFragment {
                assertTrue(
                    "${it.calendarView.height} is not less than $initialHeight",
                    it.calendarView.height < initialHeight
                )
            }
        }
        // Test height increase
        launchFragmentInContainer<EphemerisCalendarFragment>().use { scenario ->
            scenario.onFragment {
                it.calendarView.initCalendar(
                    CalendarWeekPageSource(DayOfWeek.SUNDAY),
                    BasicDateBinder()
                )
            }
            Thread.sleep(300)
            var initialHeight = 0
            scenario.onFragment {
                initialHeight = it.calendarView.height
                it.calendarView.pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
            }
            Thread.sleep(300)
            scenario.onFragment {
                assertTrue(
                    "${it.calendarView.height} is not greater than $initialHeight",
                    it.calendarView.height > initialHeight
                )
            }
        }
    }
}
