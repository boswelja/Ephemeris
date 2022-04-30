package com.boswelja.ephemeris.views

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.datetime.YearMonth
import com.boswelja.ephemeris.views.EphemerisCalendarViewActions.animateScrollTo
import com.boswelja.ephemeris.views.EphemerisCalendarViewActions.scrollTo
import com.boswelja.ephemeris.views.datebinders.BasicDateBinder
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek

@RunWith(AndroidJUnit4::class)
@LargeTest
class EphemerisCalendarViewDisplayedDateRangeTest {

    @Test
    fun scrollToDate_updatesDisplayedDateRange() {
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        var notifiedDateRange: ClosedRange<LocalDate>? = null
        calendarView.setOnDisplayedDateRangeChangeListener {
            notifiedDateRange = it
        }
        testWithScrolling(
            onScroll = { _, direction ->
                val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange
                val dateToScroll = when (direction) {
                    Direction.LeftToRight -> initialItem.start.minus(1, DateTimeUnit.DAY)
                    Direction.RightToLeft -> initialItem.endInclusive.plus(1, DateTimeUnit.DAY)
                }
                onView(withId(R.id.calendar_view)).perform(scrollTo(dateToScroll))

                // Check the displayed date range updated
                assertNotNull(notifiedDateRange)
                assertNotEquals(
                    initialItem,
                    notifiedDateRange
                )
            }
        )
    }

    @Test
    fun animateScrollToDate_updatesDisplayedDateRange() {
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        var notifiedDateRange: ClosedRange<LocalDate>? = null
        calendarView.setOnDisplayedDateRangeChangeListener {
            notifiedDateRange = it
        }
        testWithScrolling(
            onScroll = { _, direction ->
                val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange
                val dateToScroll = when (direction) {
                    Direction.LeftToRight -> initialItem.start.minus(1, DateTimeUnit.DAY)
                    Direction.RightToLeft -> initialItem.endInclusive.plus(1, DateTimeUnit.DAY)
                }
                onView(withId(R.id.calendar_view)).perform(animateScrollTo(dateToScroll))

                // Check the displayed date range updated
                assertNotNull(notifiedDateRange)
                assertNotEquals(
                    initialItem,
                    notifiedDateRange
                )
            }
        )
    }

    @Test
    fun userScrolls_updatesDisplayedDateRange() {
        // Init calendar
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        var notifiedDateRange: ClosedRange<LocalDate>? = null
        calendarView.setOnDisplayedDateRangeChangeListener {
            notifiedDateRange = it
        }
        testWithScrolling(
            onScroll = { _, direction ->
                val initialItem: ClosedRange<LocalDate> = calendarView.displayedDateRange
                val action = when (direction) {
                    Direction.LeftToRight -> swipeRight()
                    Direction.RightToLeft -> swipeLeft()
                }
                onView(withId(R.id.calendar_view)).perform(action)

                // Check the displayed date range updated
                assertNotNull(notifiedDateRange)
                assertNotEquals(
                    initialItem,
                    notifiedDateRange
                )
            }
        )
    }

    @Test
    fun scrollToDate_heightChanges() {
        // Init calendar
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY, YearMonth(2022, Month.APRIL))
        )
        onIdle()

        // Test height increase
        val preHeight = calendarView.height
        onView(withId(R.id.calendar_view)).perform(scrollTo(LocalDate(2022, Month.JULY, 1)))
        assertNotEquals(preHeight, calendarView.height)

        // Test height decrease
        onView(withId(R.id.calendar_view)).perform(scrollTo(LocalDate(2022, Month.APRIL, 1)))
        assertEquals(preHeight, calendarView.height)
    }

    @Test
    fun animateScrollToDate_heightChanges() {
        // Init calendar
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        val calendarView = scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY, YearMonth(2022, Month.APRIL))
        )
        onIdle()

        // Test height increase
        val preHeight = calendarView.height
        onView(withId(R.id.calendar_view)).perform(animateScrollTo(LocalDate(2022, Month.JULY, 1)))
        assertNotEquals(preHeight, calendarView.height)

        // Test height decrease
        onView(withId(R.id.calendar_view)).perform(animateScrollTo(LocalDate(2022, Month.APRIL, 1)))
        assertEquals(preHeight, calendarView.height)
    }

    private fun FragmentScenario<EphemerisCalendarFragment>.initAndGetCalendarView(
        pageSource: CalendarPageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY, YearMonth(2022, Month.APRIL))
    ): EphemerisCalendarView {
        var calendarView: EphemerisCalendarView? = null
        onFragment {
            it.calendarView.apply {
                calendarView = this
                animateHeight = false
                dateBinder = BasicDateBinder()
                this.pageSource = pageSource
                // TODO For some reason the initial page isn't set correctly in tests
                scrollToPosition(0)
            }
        }
        return calendarView!!
    }
}
