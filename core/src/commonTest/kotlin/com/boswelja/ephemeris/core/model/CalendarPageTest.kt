package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CalendarPageTest {

    private val testPage = calendarPage {
        rows(2) { rowNum ->
            days(3) { colNum ->
                date = LocalDate.fromEpochDays(colNum + (3 * rowNum))
                focused = true
            }
        }
    }

    @Test
    fun get_returnsCorrectDate() {
        // Check at the start of the page
        assertEquals(LocalDate.fromEpochDays(0), testPage.get(0).date)
        // Check at the end of the page
        assertEquals(LocalDate.fromEpochDays(5), testPage.get(5).date)
    }

    @Test
    fun get_throwsWhenIndexOutOfBounds() {
        assertFailsWith(IndexOutOfBoundsException::class) {
            testPage.get(-1)
        }
        assertFailsWith(IndexOutOfBoundsException::class) {
            testPage.get(6)
        }
    }

    @Test
    fun forEach_iteratesAll() {
        val iteratedDates = mutableListOf<Triple<Int, Int, CalendarDay>>()
        testPage.forEach { row, column, date ->
            iteratedDates.add(Triple(row, column, date))
        }

        // Check values are correct
        iteratedDates.forEach { (row, col, date) ->
            assertEquals(testPage.rows[row].days[col], date)
        }

        // Check we hit everything
        assertEquals(testPage.size, iteratedDates.size)
    }
}
