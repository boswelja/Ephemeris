package com.boswelja.ephemeris.views

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.datetime.YearMonth
import com.boswelja.ephemeris.views.datebinders.BasicDateBinder
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

private const val FLOW_UPDATE_TIMEOUT = 500L
private const val ANIMATION_TIMEOUT = 400L

@RunWith(AndroidJUnit4::class)
@LargeTest
class EphemerisCalendarViewDisplayedDateRangeTest {

    @Test
    fun scrollToDate_updatesDisplayedDateRange() {
        val startDate = LocalDate(2022, 4, 14)
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        // Scroll backwards
        repeat(5) { iteration ->
            // Get the current value and scroll
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
            scenario.onFragment {
                it.calendarView.scrollToDate(startDate.minus(iteration, DateTimeUnit.MONTH))
            }

            // Check the displayed date range updated
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
        // Scroll forward
        repeat(5) { iteration ->
            // Get the current value and scroll
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
            scenario.onFragment {
                it.calendarView.scrollToDate(startDate.plus(iteration, DateTimeUnit.MONTH))
            }

            // Check the displayed date range updated
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
    }

    @Test
    fun animateScrollToDate_updatesDisplayedDateRange() {
        val startDate = LocalDate(2022, Month.APRIL, 14)
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(
                firstDayOfWeek = DayOfWeek.SUNDAY,
                startYearMonth = YearMonth(2022, Month.APRIL)
            )
        )

        // Scroll backwards
        repeat(5) { iteration ->
            // Get the current value and scroll
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
            scenario.onFragment {
                it.lifecycleScope.launch {
                    it.calendarView.animateScrollToDate(startDate.minus(iteration, DateTimeUnit.MONTH))
                }
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            // Check the displayed date range updated
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
        // Scroll forward
        repeat(5) { iteration ->
            // Get the current value and scroll
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
            scenario.onFragment {
                it.lifecycleScope.launch {
                    it.calendarView.animateScrollToDate(startDate.plus(iteration, DateTimeUnit.MONTH))
                }
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            // Check the displayed date range updated
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
    }

    @Test
    fun userScrolls_updatesDisplayedDateRange() {
        // Init calendar
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        // Scrolling forward
        repeat(5) {
            // Get the current displayed date range
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value

            // Perform swipe and assert state
            onView(withId(R.id.calendar_view)).perform(swipeLeft())
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
        // Scrolling backward
        repeat(5) {
            // Get the current displayed date range
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value

            // Perform swipe and assert state
            onView(withId(R.id.calendar_view)).perform(swipeRight())
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
        // Mixed scrolling
        repeat(10) {
            // Get the current displayed date range
            val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value

            // Perform swipe and assert state
            val action = if (it % 2 == 0) swipeLeft() else swipeRight()
            onView(withId(R.id.calendar_view)).perform(action)
            assertNotEquals(
                initialItem,
                calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
            )
        }
    }

    private fun FragmentScenario<EphemerisCalendarFragment>.initAndGetCalendarView(
        pageSource: CalendarPageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY, YearMonth(2022, Month.APRIL))
    ): EphemerisCalendarView {
        var calendarView: EphemerisCalendarView? = null
        onFragment {
            calendarView = it.calendarView
            calendarView!!.initCalendar(pageSource, BasicDateBinder())
        }
        return calendarView!!
    }
}
