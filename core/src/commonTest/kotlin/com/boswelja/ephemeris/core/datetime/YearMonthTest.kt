package com.boswelja.ephemeris.core.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals

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
        assertEquals(
            LocalDate(2000, Month.FEBRUARY, 29),
            YearMonth(2000, Month.FEBRUARY).endDate
        )
        // Check normal Feb
        assertEquals(
            LocalDate(2001, Month.FEBRUARY, 28),
            YearMonth(2001, Month.FEBRUARY).endDate
        )
        // Check 31 days
        assertEquals(
            LocalDate(2019, Month.JANUARY, 31),
            YearMonth(2019, Month.JANUARY).endDate
        )
        // Check 30 days
        assertEquals(
            LocalDate(2100, Month.APRIL, 30),
            YearMonth(2100, Month.APRIL).endDate
        )
    }

    @Test
    fun plus_adjustsMonthCorrectly() {
        // Check passing 0
        assertEquals(
            Month.JULY,
            YearMonth(2021, Month.JULY).plus(0).month
        )
        // Check incrementing by 1
        assertEquals(
            Month.JULY,
            YearMonth(2021, Month.JUNE).plus(1).month
        )
        // Check decrementing by 1
        assertEquals(
            Month.MAY,
            YearMonth(2021, Month.JUNE).plus(-1).month
        )
        // Check incrementing by a large number
        assertEquals(
            Month.OCTOBER,
            YearMonth(2021, Month.JUNE).plus(100).month
        )
        // Check decrementing by a large number
        assertEquals(
            Month.MARCH,
            YearMonth(2021, Month.JULY).plus(-100).month
        )
    }

    @Test
    fun plus_adjustsYearCorrectly() {
        // Check passing 0
        assertEquals(
            2021,
            YearMonth(2021, Month.JULY).plus(0).year
        )
        // Check incrementing into new year by 1
        assertEquals(
            2022,
            YearMonth(2021, Month.DECEMBER).plus(1).year
        )
        // Check decrementing into new year by 1
        assertEquals(
            2021,
            YearMonth(2022, Month.JANUARY).plus(-1).year
        )
        // Check incrementing by a large number
        assertEquals(
            2030,
            YearMonth(2022, Month.JULY).plus(100).year
        )
        // Check decrementing by a large number
        assertEquals(
            2014,
            YearMonth(2022, Month.JULY).plus(-100).year
        )
    }

    @Test
    fun until_returnsCorrectValues() {
        assertEquals(
            0,
            YearMonth(2022, Month.MARCH).until(YearMonth(2022, Month.MARCH))
        )
        assertEquals(
            4,
            YearMonth(2022, Month.MARCH).until(YearMonth(2022, Month.JULY))
        )
        assertEquals(
            -2,
            YearMonth(2022, Month.MARCH).until(YearMonth(2022, Month.JANUARY))
        )
        assertEquals(
            12,
            YearMonth(2022, Month.MARCH).until(YearMonth(2023, Month.MARCH))
        )
        assertEquals(
            -10,
            YearMonth(2022, Month.MARCH).until(YearMonth(2021, Month.MAY))
        )
    }
}
