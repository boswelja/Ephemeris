package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class YearMonthTest {

    @Test
    fun startDate_isCorrect() {
        (1990..2200).forEach { year ->
            Month.values().forEach { month ->
                val yearMonth = YearMonth(year, month)
                assertEquals(1, yearMonth.startDate.dayOfMonth)
                assertEquals(month, yearMonth.startDate.month)
                assertEquals(year, yearMonth.startDate.year)
            }
        }
    }

    @Test
    fun endDate_isCorrect() {
        // Check leap day
        assertEndDate(
            LocalDate(2000, Month.FEBRUARY, 29),
            YearMonth(2000, Month.FEBRUARY)
        )
        // Check normal Feb
        assertEndDate(
            LocalDate(2001, Month.FEBRUARY, 28),
            YearMonth(2001, Month.FEBRUARY)
        )
        // Check 31 days
        assertEndDate(
            LocalDate(2019, Month.JANUARY, 31),
            YearMonth(2019, Month.JANUARY)
        )
        // Check 30 days
        assertEndDate(
            LocalDate(2100, Month.APRIL, 30),
            YearMonth(2100, Month.APRIL)
        )
    }

    @Test
    fun plusMonths_updatesValues() {
        val initialMonth = YearMonth(2000, Month.FEBRUARY)
        val newMonth = initialMonth.plusMonths(10)
        assertNotEquals(initialMonth.startDate, newMonth.startDate)
        assertNotEquals(initialMonth.endDate, newMonth.endDate)
    }

    private fun assertEndDate(
        expected: LocalDate,
        yearMonth: YearMonth
    ) {
        assertEquals(expected, yearMonth.endDate)
    }
}
