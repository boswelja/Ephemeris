package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeExtTest {

    @Test
    fun plusMonths_correctlyAddsMonths() {
        // Check wrapping to first month
        assertEquals(
            Month.JANUARY,
            Month.DECEMBER.plusMonths(1)
        )
        // Check adding large number
        assertEquals(
            Month.AUGUST,
            Month.JANUARY.plusMonths(175)
        )
        // Check adding within the current range
        assertEquals(
            Month.JULY,
            Month.MARCH.plusMonths(4)
        )
    }

    @Test
    fun plusMonths_correctlySubtractsMonths() {
        // Check wrapping to last month
        assertEquals(
            Month.DECEMBER,
            Month.JANUARY.plusMonths(-1)
        )
        // Check subtracting large number
        assertEquals(
            Month.JUNE,
            Month.JANUARY.plusMonths(-175)
        )
        // Check subtracting within the current range
        assertEquals(
            Month.APRIL,
            Month.AUGUST.plusMonths(-4)
        )
    }

    @Test
    fun minusDays_correctlySubtractsDays() {
        // Check wrapping to end of week
        assertEquals(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY.minusDays(1)
        )
        // Check subtracting within the current range
        assertEquals(
            DayOfWeek.TUESDAY,
            DayOfWeek.SATURDAY.minusDays(4)
        )
        // Check subtracting a big number
        assertEquals(
            DayOfWeek.TUESDAY,
            DayOfWeek.SATURDAY.minusDays(165)
        )
    }
}
