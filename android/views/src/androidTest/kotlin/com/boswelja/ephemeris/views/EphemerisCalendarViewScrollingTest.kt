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
import com.boswelja.ephemeris.views.EphemerisCalendarViewActions.scrollTo
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
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        testWithScrolling(
            onScroll = { _, direction ->
                val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
                val dateToScroll = when (direction) {
                    Direction.LeftToRight -> initialItem.start.minus(1, DateTimeUnit.DAY)
                    Direction.RightToLeft -> initialItem.endInclusive.plus(1, DateTimeUnit.DAY)
                }
                onView(withId(R.id.calendar_view)).perform(scrollTo(dateToScroll))

                // Check the displayed date range updated
                assertNotEquals(
                    initialItem,
                    calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
                )
            }
        )
    }

    @Test
    fun animateScrollToDate_updatesDisplayedDateRange() {
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        testWithScrolling(
            onScroll = { _, direction ->
                val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
                val dateToScroll = when (direction) {
                    Direction.LeftToRight -> initialItem.start.minus(1, DateTimeUnit.DAY)
                    Direction.RightToLeft -> initialItem.endInclusive.plus(1, DateTimeUnit.DAY)
                }
                scenario.onFragment {
                    it.lifecycleScope.launch {
                        it.calendarView.animateScrollToDate(dateToScroll)
                    }
                }
                Thread.sleep(ANIMATION_TIMEOUT)

                // Check the displayed date range updated
                assertNotEquals(
                    initialItem,
                    calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
                )
            }
        )
    }

    @Test
    fun userScrolls_updatesDisplayedDateRange() {
        // Init calendar
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        testWithScrolling(
            onScroll = { _, direction ->
                val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange.value
                val action = when (direction) {
                    Direction.LeftToRight -> swipeRight()
                    Direction.RightToLeft -> swipeLeft()
                }
                onView(withId(R.id.calendar_view)).perform(action)

                // Check the displayed date range updated
                assertNotEquals(
                    initialItem,
                    calendarView.displayedDateRange.getOrAwaitValue(FLOW_UPDATE_TIMEOUT)
                )
            }
        )
    }

    private fun FragmentScenario<EphemerisCalendarFragment>.initAndGetCalendarView(
        pageSource: CalendarPageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY, YearMonth(2022, Month.APRIL))
    ): EphemerisCalendarView {
        var calendarView: EphemerisCalendarView? = null
        onFragment {
            calendarView = it.calendarView
            it.calendarView.animateHeight = false
            it.calendarView.initCalendar(pageSource, BasicDateBinder())
        }
        return calendarView!!
    }
}
