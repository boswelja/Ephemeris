package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalDateExtTest {

    @Test
    fun startOfWeek_returnsDateAtStartOfWeek() {
        // Check when it's already the first day of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 14),
            LocalDate(2022, Month.FEBRUARY, 14).startOfWeek(DayOfWeek.MONDAY)
        )
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 13),
            LocalDate(2022, Month.FEBRUARY, 13).startOfWeek(DayOfWeek.SUNDAY)
        )
        // Check when it's just after the first day of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 14),
            LocalDate(2022, Month.FEBRUARY, 15).startOfWeek(DayOfWeek.MONDAY)
        )
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 13),
            LocalDate(2022, Month.FEBRUARY, 14).startOfWeek(DayOfWeek.SUNDAY)
        )
        // Check when it's at the end of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 14),
            LocalDate(2022, Month.FEBRUARY, 20).startOfWeek(DayOfWeek.MONDAY)
        )
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 13),
            LocalDate(2022, Month.FEBRUARY, 19).startOfWeek(DayOfWeek.SUNDAY)
        )
        // Check when it's the middle of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 14),
            LocalDate(2022, Month.FEBRUARY, 17).startOfWeek(DayOfWeek.MONDAY)
        )
    }

    @Test
    fun endOfWeek_returnsDateAtEndOfWeek() {
        // Check when it's already the last day of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 20),
            LocalDate(2022, Month.FEBRUARY, 20).endOfWeek(DayOfWeek.MONDAY)
        )
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 19),
            LocalDate(2022, Month.FEBRUARY, 19).endOfWeek(DayOfWeek.SUNDAY)
        )
        // Check when it's just before the last day of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 20),
            LocalDate(2022, Month.FEBRUARY, 19).endOfWeek(DayOfWeek.MONDAY)
        )
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 19),
            LocalDate(2022, Month.FEBRUARY, 18).endOfWeek(DayOfWeek.SUNDAY)
        )
        // Check when it's at the start of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 20),
            LocalDate(2022, Month.FEBRUARY, 14).endOfWeek(DayOfWeek.MONDAY)
        )
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 19),
            LocalDate(2022, Month.FEBRUARY, 13).endOfWeek(DayOfWeek.SUNDAY)
        )
        // Check when it's the middle of the week
        assertEquals(
            LocalDate(2022, Month.FEBRUARY, 20),
            LocalDate(2022, Month.FEBRUARY, 17).endOfWeek(DayOfWeek.MONDAY)
        )
    }
}
