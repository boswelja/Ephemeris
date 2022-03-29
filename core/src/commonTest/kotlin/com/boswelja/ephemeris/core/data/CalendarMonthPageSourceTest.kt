package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.datetime.YearMonth
import com.boswelja.ephemeris.core.datetime.plus
import com.boswelja.ephemeris.core.datetime.yearMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalendarMonthPageSourceTest {

    @Test
    fun loadPageData_respectsFirstDayOfWeek() {
        val daysOfWeek = DayOfWeek.values()

        daysOfWeek.forEach { firstDayOfWeek ->
            val source = CalendarMonthPageSource(
                firstDayOfWeek = firstDayOfWeek
            )
            // Load some pages
            (-10..10).forEach { page ->
                val calendarPage = source.loadPageData(page)

                // Check each row starts on the correct day
                assertTrue {
                    calendarPage.rows.all { it.days.first().date.dayOfWeek == firstDayOfWeek }
                }
            }
        }
    }

    @Test
    fun loadPageData_respectsMonthFocusMode() {
        val startYearMonth = YearMonth(2022, Month.MARCH)
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            startYearMonth = startYearMonth,
            focusMode = CalendarMonthPageSource.FocusMode.MONTH
        )
        (-10..10).forEach { page ->
            val yearMonth = startYearMonth.plus(page)
            val calendarPage = source.loadPageData(page)
            calendarPage.rows.forEach { row ->
                row.days.forEach {
                    assertEquals(
                        it.date.yearMonth == yearMonth,
                        it.isFocusedDate
                    )
                }
            }
        }
    }

    @Test
    fun loadPageData_respectsAllFocusMode() {
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = CalendarMonthPageSource.FocusMode.ALL
        )
        (-10..10).forEach { page ->
            val calendarPage = source.loadPageData(page)
            calendarPage.rows.forEach { row ->
                row.days.forEach {
                    assertTrue(it.isFocusedDate)
                }
            }
        }
    }

    @Test
    fun loadPageData_respectsNoneFocusMode() {
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = CalendarMonthPageSource.FocusMode.NONE
        )
        (-10..10).forEach { page ->
            val calendarPage = source.loadPageData(page)
            calendarPage.rows.forEach { row ->
                row.days.forEach {
                    assertFalse(it.isFocusedDate)
                }
            }
        }
    }

    @Test
    fun loadPageData_respectsWeekdayFocusMode() {
        val startYearMonth = YearMonth(2022, Month.MARCH)
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            startYearMonth = startYearMonth,
            focusMode = CalendarMonthPageSource.FocusMode.WEEKDAYS
        )
        val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        (-10..10).forEach { page ->
            val calendarPage = source.loadPageData(page)
            calendarPage.rows.forEach { row ->
                row.days.forEach {
                    assertEquals(
                        !weekends.contains(it.date.dayOfWeek),
                        it.isFocusedDate
                    )
                }
            }
        }
    }

    @Test
    fun loadPageData_succeedsOnExtremes() {
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY
        )
        source.loadPageData(Int.MIN_VALUE)
        source.loadPageData(Int.MAX_VALUE)
        source.loadPageData(0)
    }

    @Test
    fun loadPageData_loadsCorrectNumberOfRows() {
        val startYearMonth = YearMonth(2022, Month.MARCH)
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.MONDAY,
            startYearMonth = startYearMonth
        )

        assertEquals(
            5,
            source.loadPageData(0).rows.size
        )
        assertEquals(
            6,
            source.loadPageData(2).rows.size
        )
        assertEquals(
            4,
            source.loadPageData(-13).rows.size
        )
    }

    @Test
    fun getPageFor_returnsCorrectPage() {
        val startYearMonth = YearMonth(2022, Month.MARCH)
        val source = CalendarMonthPageSource(
            firstDayOfWeek = DayOfWeek.MONDAY,
            startYearMonth = startYearMonth
        )

        assertEquals(
            0,
            source.getPageFor(LocalDate(2022, Month.MARCH, 1))
        )
        assertEquals(
            0,
            source.getPageFor(LocalDate(2022, Month.MARCH, 31))
        )
        assertEquals(
            1,
            source.getPageFor(LocalDate(2022, Month.APRIL, 1))
        )
        assertEquals(
            -1,
            source.getPageFor(LocalDate(2022, Month.FEBRUARY, 28))
        )
        assertEquals(
            -13,
            source.getPageFor(LocalDate(2021, Month.FEBRUARY, 20))
        )
        assertEquals(
            10,
            source.getPageFor(LocalDate(2023, Month.JANUARY, 3))
        )
    }
}
