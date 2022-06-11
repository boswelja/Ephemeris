package com.boswelja.ephemeris.views

import android.graphics.Color
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.datetime.yearMonth
import com.boswelja.ephemeris.views.CustomViewMatchers.hasChild
import com.boswelja.ephemeris.views.CustomViewMatchers.withBackgroundColor
import com.boswelja.ephemeris.views.EphemerisCalendarViewActions.scrollTo
import com.boswelja.ephemeris.views.datebinders.ChangeableDateBinder
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import kotlin.properties.Delegates

@RunWith(AndroidJUnit4::class)
@LargeTest
class EphemerisCalendarViewDateChangeTest {

    private var backgroundColor by Delegates.notNull<Int>()

    @Before
    fun setUp() {
        backgroundColor = Color.RED
    }

    @Test
    fun notifyDateChanged_correctlyReBindsCell() {
        val startDate = LocalDate(2022, 4, 19)
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(
                firstDayOfWeek = DayOfWeek.SUNDAY,
                startYearMonth = startDate.yearMonth
            )
        )

        onIdle()

        backgroundColor = Color.GREEN
        scenario.onFragment {
            it.calendarView.notifyDateChanged(startDate)
        }
        Thread.sleep(300)

        // Check the date cell changed
        onView(allOf(isCompletelyDisplayed(), hasChild(withText(startDate.dayOfMonth.toString()))))
            .check(matches(withBackgroundColor(Color.GREEN)))

        // Check the before/after cells did not change
        onView(allOf(isCompletelyDisplayed(), hasChild(withText((startDate.dayOfMonth - 1).toString()))))
            .check(matches(withBackgroundColor(Color.RED)))
        onView(allOf(isCompletelyDisplayed(), hasChild(withText((startDate.dayOfMonth + 1).toString()))))
            .check(matches(withBackgroundColor(Color.RED)))
    }

    @Test
    fun notifyDateRangeChanged_correctlyRebindsCells() {
        val startDate = LocalDate(2022, 4, 9)
        val targetRange = startDate..startDate.plus(10, DateTimeUnit.DAY)
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(
                firstDayOfWeek = DayOfWeek.SUNDAY,
                startYearMonth = startDate.yearMonth
            )
        )

        onIdle()

        backgroundColor = Color.GREEN
        scenario.onFragment {
            it.calendarView.notifyDateRangeChanged(targetRange)
        }
        Thread.sleep(300)

        // Check the date cells in range changed
        onView(allOf(isCompletelyDisplayed(), hasChild(withText(targetRange.start.dayOfMonth.toString()))))
            .check(matches(withBackgroundColor(Color.GREEN)))
        onView(allOf(isCompletelyDisplayed(), hasChild(withText(targetRange.endInclusive.dayOfMonth.toString()))))
            .check(matches(withBackgroundColor(Color.GREEN)))

        // Check the date cells before/after did not change
        onView(allOf(isCompletelyDisplayed(), hasChild(withText((targetRange.start.dayOfMonth - 1).toString()))))
            .check(matches(withBackgroundColor(Color.RED)))
        onView(allOf(isCompletelyDisplayed(), hasChild(withText((targetRange.endInclusive.dayOfMonth + 1).toString()))))
            .check(matches(withBackgroundColor(Color.RED)))
    }

    @Test
    fun notifyDateChange_updatesOverlappingDates() {
        // Init test
        val startDate = LocalDate(2022, 5, 31)
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(
                firstDayOfWeek = DayOfWeek.SUNDAY,
                startYearMonth = startDate.yearMonth
            )
        )
        onIdle()

        // Update the date cell
        backgroundColor = Color.GREEN
        scenario.onFragment {
            it.calendarView.notifyDateChanged(startDate)
        }
        Thread.sleep(300)

        // Scroll to the next page
        onView(withId(R.id.calendar_view)).perform(scrollTo(startDate.plus(1, DateTimeUnit.WEEK)))

        // Check date cell changed
        onView(allOf(isCompletelyDisplayed(), hasChild(withText(startDate.dayOfMonth.toString()))))
            .check(matches(withBackgroundColor(Color.GREEN)))
    }

    @Test
    fun notifyDateRangeChange_updatesOverlappingDates() {
        // Init test
        val startDate = LocalDate(2022, 5, 31)
        val targetRange = startDate.minus(10, DateTimeUnit.DAY)..startDate
        val scenario = launchFragmentInContainer<EphemerisCalendarFragment>()
        scenario.initAndGetCalendarView(
            pageSource = CalendarMonthPageSource(
                firstDayOfWeek = DayOfWeek.SUNDAY,
                startYearMonth = startDate.yearMonth
            )
        )
        onIdle()

        // Update the date cells
        backgroundColor = Color.GREEN
        scenario.onFragment {
            it.calendarView.notifyDateRangeChanged(targetRange)
        }
        Thread.sleep(300)

        // Scroll to the next page
        onView(withId(R.id.calendar_view)).perform(scrollTo(startDate.plus(1, DateTimeUnit.WEEK)))

        // Check date cell changed
        onView(allOf(isCompletelyDisplayed(), hasChild(withText(startDate.dayOfMonth.toString()))))
            .check(matches(withBackgroundColor(Color.GREEN)))
    }

    private fun FragmentScenario<EphemerisCalendarFragment>.initAndGetCalendarView(
        pageSource: CalendarPageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
    ): EphemerisCalendarView {
        var calendarView: EphemerisCalendarView? = null
        onFragment {
            calendarView = it.calendarView
            it.calendarView.apply {
                calendarView = this
                dateBinder = ChangeableDateBinder { backgroundColor }
                this.pageSource = pageSource
            }
        }
        return calendarView!!
    }
}
