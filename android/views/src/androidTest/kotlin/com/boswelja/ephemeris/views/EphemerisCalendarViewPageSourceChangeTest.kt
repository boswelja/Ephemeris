package com.boswelja.ephemeris.views

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import com.boswelja.ephemeris.views.datebinders.BasicDateBinder
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

private const val ANIMATION_TIMEOUT = 300L

@RunWith(AndroidJUnit4::class)
@LargeTest
class EphemerisCalendarViewPageSourceChangeTest {

    @Test
    fun onPageSourceChange_heightChanges() {
        // Test height decrease
        launchFragmentInContainer<EphemerisCalendarFragment>().use { scenario ->
            // Init calendar
            val calendarView = scenario.initAndGetCalendarView(CalendarMonthPageSource(DayOfWeek.SUNDAY))
            Thread.sleep(ANIMATION_TIMEOUT)

            // Get initial height and change page source
            val initialHeight = calendarView.height
            scenario.onFragment {
                calendarView.pageSource = CalendarWeekPageSource(DayOfWeek.SUNDAY)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            // Assert result
            assertTrue(
                "${calendarView.height} is not less than $initialHeight",
                calendarView.height < initialHeight
            )
        }
        // Test height increase
        launchFragmentInContainer<EphemerisCalendarFragment>().use { scenario ->
            // Init calendar
            val calendarView = scenario.initAndGetCalendarView(CalendarWeekPageSource(DayOfWeek.SUNDAY))
            Thread.sleep(ANIMATION_TIMEOUT)

            // Get initial height and change page source
            val initialHeight = calendarView.height
            scenario.onFragment {
                calendarView.pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            // Assert result
            assertTrue(
                "${calendarView.height} is not greater than $initialHeight",
                calendarView.height > initialHeight
            )
        }
    }

    private fun FragmentScenario<EphemerisCalendarFragment>.initAndGetCalendarView(
        pageSource: CalendarPageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
    ): EphemerisCalendarView {
        var calendarView: EphemerisCalendarView? = null
        onFragment {
            calendarView = it.calendarView
            calendarView!!.initCalendar(pageSource, BasicDateBinder())
        }
        return calendarView!!
    }
}
