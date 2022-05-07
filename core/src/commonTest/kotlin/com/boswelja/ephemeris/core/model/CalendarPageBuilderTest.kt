package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CalendarPageBuilderTest {

    @Test
    fun build_failsWithNoRows() {
        val builder = CalendarPageBuilder()
        assertFails {
            builder.build()
        }
    }

    @Test
    fun build_correctlyBuildsPage() {
        val calendarPage = CalendarPage(
            (0 until 6).map {
                CalendarRow(
                    (0 until 7).map {
                        CalendarDay(
                            date = LocalDate(2022, 3, 26),
                            isFocusedDate = true
                        )
                    }
                )
            }
        )
        val builder = CalendarPageBuilder().apply {
            rows(calendarPage.rows.size) { index ->
                days(calendarPage.rows[index].days.size) {
                    date = LocalDate(2022, 3, 26)
                    focused = true
                }
            }
        }
        assertEquals(
            calendarPage,
            builder.build()
        )
    }

    @Test
    fun row_addsSingleRow() {
        val page = CalendarPageBuilder().apply {
            row {
                day {
                    date = LocalDate(2022, 6, 26)
                    focused = true
                }
            }
        }.build()

        assertEquals(
            1,
            page.rows.size
        )
    }

    @Test
    fun rows_addsCorrectNumberOfRows() {
        val testCounts = 1..6

        testCounts.forEach { count ->
            val calendarPage = CalendarPageBuilder().apply {
                rows(count) {
                    days(7) {
                        date = LocalDate(2022, 3, 26)
                        focused = true
                    }
                }
            }.build()
            assertEquals(
                count,
                calendarPage.rows.size
            )
        }
    }
}

class CalendarRowBuilderTest {

    @Test
    fun build_failsWithNoDays() {
        val builder = CalendarRowBuilder() // Do nothing with it
        assertFails {
            builder.build()
        }
    }

    @Test
    fun build_correctlyBuildsRow() {
        val calendarRow = CalendarRow(
            (0 until 7).map {
                CalendarDay(
                    date = LocalDate(2022, 3, 26),
                    isFocusedDate = true
                )
            }
        )

        val builder = CalendarRowBuilder().apply {
            days(calendarRow.days.size) {
                date = LocalDate(2022, 3, 26)
                focused = true
            }
        }

        assertEquals(
            calendarRow,
            builder.build()
        )
    }

    @Test
    fun days_addsCorrectNumberOfDays() {
        val testCounts = 1..6

        testCounts.forEach { count ->
            val calendarRow = CalendarRowBuilder().apply {
                days(count) {
                    date = LocalDate(2022, 3, 26)
                    focused = true
                }
            }.build()
            assertEquals(
                count,
                calendarRow.days.size
            )
        }
    }

    @Test
    fun day_addsSingleDay() {
        val calendarRow = CalendarRowBuilder().apply {
            day {
                date = LocalDate(2022, 3, 26)
                focused = true
            }
        }.build()
        assertEquals(
            1,
            calendarRow.days.size
        )
    }
}

class CalendarDayBuilderTest {

    @Test
    fun build_failsWhenDateMissing() {
        val builder = CalendarDayBuilder().apply {
            focused = true
        }
        assertFails {
            builder.build()
        }
    }

    @Test
    fun build_failsWhenFocusMissing() {
        val builder = CalendarDayBuilder().apply {
            date = LocalDate(2022, 3, 26)
        }
        assertFails {
            builder.build()
        }
    }

    @Test
    fun build_correctlyBuildsCalendarDay() {
        val calendarDay = CalendarDay(
            date = LocalDate(2022, 3, 26),
            isFocusedDate = true
        )
        val builder = CalendarDayBuilder().apply {
            focused = calendarDay.isFocusedDate
            date = calendarDay.date
        }
        assertEquals(
            calendarDay,
            builder.build()
        )
    }
}
