package com.boswelja.ephemeris.core.model

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CalendarPageTest {

    @Test
    fun getFlatIndexOf_returnsCorrectForOutOfBounds() {
        val page = calendarPage {
            rows(6) { row ->
                val startDate = LocalDate(2022, Month.MAY, 8).plus(row, DateTimeUnit.WEEK)
                days(7) { col ->
                    date = startDate.plus(col, DateTimeUnit.DAY)
                    focused = true
                }
            }
        }
        val firstDate = page.rows.first().days.first()
        val lastDate = page.rows.last().days.last()

        // Check one before the first date
        assertEquals(
            -1,
            page.getFlatIndexOf(firstDate.date.minus(1, DateTimeUnit.DAY))
        )
        // Check one after the last date
        assertEquals(
            -1,
            page.getFlatIndexOf(lastDate.date.plus(1, DateTimeUnit.DAY))
        )
    }

    @Test
    fun getFlatIndexOf_returnsCorrectForInBounds() {
        val page = calendarPage {
            rows(6) { row ->
                val startDate = LocalDate(2022, Month.MAY, 8).plus(row, DateTimeUnit.WEEK)
                days(7) { col ->
                    date = startDate.plus(col, DateTimeUnit.DAY)
                    focused = true
                }
            }
        }
        val firstDate = page.rows.first().days.first()
        val lastDate = page.rows.last().days.last()

        // Check first date
        assertEquals(
            0,
            page.getFlatIndexOf(firstDate.date)
        )
        // Check last date
        assertEquals(
            41,
            page.getFlatIndexOf(lastDate.date)
        )
        // Check arbitrary row
        assertEquals(
            7,
            page.getFlatIndexOf(page.rows[1].days.first().date)
        )
    }

    @Test
    fun getDateForFlatIndex_throwsWhenOutOfBounds() {
        val page = calendarPage {
            rows(6) { row ->
                val startDate = LocalDate(2022, Month.MAY, 8).plus(row, DateTimeUnit.WEEK)
                days(7) { col ->
                    date = startDate.plus(col, DateTimeUnit.DAY)
                    focused = true
                }
            }
        }

        // Check for one before start
        assertFails {
            page.getDateForFlatIndex(-1)
        }
        // Check for one after end
        assertFails {
            page.getDateForFlatIndex(42)
        }
    }

    @Test
    fun getDateForFlatIndex_returnsWhenInBounds() {
        val page = calendarPage {
            rows(6) { row ->
                val startDate = LocalDate(2022, Month.MAY, 8).plus(row, DateTimeUnit.WEEK)
                days(7) { col ->
                    date = startDate.plus(col, DateTimeUnit.DAY)
                    focused = true
                }
            }
        }
        val firstDate = page.rows.first().days.first()
        val lastDate = page.rows.last().days.last()

        // Check first date
        assertEquals(
            firstDate,
            page.getDateForFlatIndex(0)
        )
        // Check last date
        assertEquals(
            lastDate,
            page.getDateForFlatIndex(41)
        )
        // Check arbitrary row
        assertEquals(
            page.rows[1].days.first(),
            page.getDateForFlatIndex(7)
        )
    }
}
