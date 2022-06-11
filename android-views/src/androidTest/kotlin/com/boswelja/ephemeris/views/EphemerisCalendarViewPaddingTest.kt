package com.boswelja.ephemeris.views

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import com.boswelja.ephemeris.views.CustomViewMatchers.withClipToPadding
import com.boswelja.ephemeris.views.CustomViewMatchers.withPadding
import com.boswelja.ephemeris.views.datebinders.BasicDateBinder
import com.boswelja.ephemeris.views.pager.HeightAdjustingPager
import kotlinx.datetime.DayOfWeek
import org.junit.Assert.assertEquals
import org.junit.Test

class EphemerisCalendarViewPaddingTest {

    @Test
    fun xmlPadding_isPropagatedToViews() {
        // This Fragment has padding by default
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            ApplicationProvider.getApplicationContext<Context>().resources.displayMetrics
        ).toInt()
        val scenario = launchFragmentInContainer<EphemerisCalendarWithPaddingFragment>()
        scenario.initAndGetCalendarView()

        onView(isAssignableFrom(HeightAdjustingPager::class.java))
            .check(matches(withPadding(left = padding, top = 0, right = padding, bottom = 0)))

        // Now change page source
        scenario.onFragment {
            it.calendarView.pageSource = CalendarWeekPageSource(DayOfWeek.SUNDAY)
        }
        onIdle()

        onView(isAssignableFrom(HeightAdjustingPager::class.java))
            .check(matches(withPadding(left = padding, top = 0, right = padding, bottom = 0)))
    }

    @Test
    fun getPadding_returnsValuesFromViewsCorrectly() {
        // This Fragment has padding by default
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            ApplicationProvider.getApplicationContext<Context>().resources.displayMetrics
        ).toInt()
        val scenario = launchFragmentInContainer<EphemerisCalendarWithPaddingFragment>()
        val calendarView = scenario.initAndGetCalendarView()

        onIdle()

        assertEquals(padding, calendarView.paddingTop)
        assertEquals(padding, calendarView.paddingLeft)
        assertEquals(padding, calendarView.paddingRight)
        assertEquals(padding, calendarView.paddingBottom)
        assertEquals(padding, calendarView.paddingStart)
        assertEquals(padding, calendarView.paddingEnd)

        // Try change padding
        val newPadding = padding * 2
        scenario.onFragment {
            it.calendarView.setPadding(newPadding, newPadding, newPadding, newPadding)
        }

        assertEquals(newPadding, calendarView.paddingTop)
        assertEquals(newPadding, calendarView.paddingLeft)
        assertEquals(newPadding, calendarView.paddingRight)
        assertEquals(newPadding, calendarView.paddingBottom)
        assertEquals(newPadding, calendarView.paddingStart)
        assertEquals(newPadding, calendarView.paddingEnd)
    }

    @Test
    fun clipToPadding_isPropagatedToViews() {
        // This Fragment has padding by default
        val scenario = launchFragmentInContainer<EphemerisCalendarWithPaddingFragment>()
        val calendarView = scenario.initAndGetCalendarView()
        calendarView.clipToPadding = true
        onIdle()

        onView(isAssignableFrom(HeightAdjustingPager::class.java))
            .check(matches(withClipToPadding(true)))

        // Now change page source
        scenario.onFragment {
            it.calendarView.pageSource = CalendarWeekPageSource(DayOfWeek.SUNDAY)
        }
        onIdle()

        onView(isAssignableFrom(HeightAdjustingPager::class.java))
            .check(matches(withClipToPadding(true)))
    }

    private fun FragmentScenario<EphemerisCalendarWithPaddingFragment>.initAndGetCalendarView(
        pageSource: CalendarPageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
    ): EphemerisCalendarView {
        var calendarView: EphemerisCalendarView? = null
        onFragment {
            it.calendarView.apply {
                calendarView = this
                dateBinder = BasicDateBinder()
                this.pageSource = pageSource
            }
        }
        return calendarView!!
    }
}
