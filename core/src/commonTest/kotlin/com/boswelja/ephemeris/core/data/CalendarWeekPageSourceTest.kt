package com.boswelja.ephemeris.core.data

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalendarWeekPageSourceTest {

    @Test
    fun loadPageData_respectsFirstDayOfWeek() {
        val daysOfWeek = DayOfWeek.values()

        daysOfWeek.forEach { firstDayOfWeek ->
            val source = CalendarWeekPageSource(
                firstDayOfWeek = firstDayOfWeek
            )
            // Load some pages
            (-10..10).forEach { page ->
                val calendarPage = source.loadPageData(page)

                // Check each row starts on the correct day
                assertTrue {
                    calendarPage.rows.first().days.first().date.dayOfWeek == firstDayOfWeek
                }
            }
        }
    }

    @Test
    fun loadPageData_respectsAllFocusMode() {
        val source = CalendarWeekPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = CalendarWeekPageSource.FocusMode.ALL
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
        val source = CalendarWeekPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = CalendarWeekPageSource.FocusMode.NONE
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
        val source = CalendarWeekPageSource(
            firstDayOfWeek = DayOfWeek.SUNDAY,
            focusMode = CalendarWeekPageSource.FocusMode.WEEKDAYS
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
    fun loadPageData_loadsSingleRow() {
        val source = CalendarWeekPageSource(
            firstDayOfWeek = DayOfWeek.MONDAY,
        )

        assertEquals(
            1,
            source.loadPageData(0).rows.size
        )
        assertEquals(
            1,
            source.loadPageData(2).rows.size
        )
        assertEquals(
            1,
            source.loadPageData(-13).rows.size
        )
    }

    @Test
    fun getPageFor_returnsCorrectPage() {
        val startDate = LocalDate(2022, Month.MARCH, 23)
        val source = CalendarWeekPageSource(
            firstDayOfWeek = DayOfWeek.MONDAY,
            startDate = startDate
        )

        assertEquals(
            0,
            source.getPageFor(LocalDate(2022, Month.MARCH, 21))
        )
        assertEquals(
            0,
            source.getPageFor(LocalDate(2022, Month.MARCH, 27))
        )
        assertEquals(
            -13,
            source.getPageFor(LocalDate(2021, Month.DECEMBER, 22))
        )
        assertEquals(
            10,
            source.getPageFor(LocalDate(2022, Month.JUNE, 3))
        )
    }
}
