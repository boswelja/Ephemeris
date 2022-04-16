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
        // Init calendar
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView(CalendarMonthPageSource(DayOfWeek.SUNDAY))
        Thread.sleep(ANIMATION_TIMEOUT)

        // Test height decrease
        calendarView.height.also { bigHeight ->
            scenario.onFragment {
                it.calendarView.pageSource = CalendarWeekPageSource(DayOfWeek.SUNDAY)
            }
            Thread.sleep(ANIMATION_TIMEOUT)
            val littleHeight = calendarView.height
            assertTrue(
                "$littleHeight is not less than $bigHeight",
                littleHeight < bigHeight
            )
        }

        // Test height increase
        calendarView.height.also { littleHeight ->
            scenario.onFragment {
                it.calendarView.pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
            }
            Thread.sleep(ANIMATION_TIMEOUT)
            val bigHeight = calendarView.height
            assertTrue(
                "$littleHeight is not less than $bigHeight",
                littleHeight < bigHeight
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
