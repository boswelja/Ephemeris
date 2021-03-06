package com.boswelja.ephemeris.views

import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.calendarPage
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarPageExtTest {

    @Test
    fun forEachInRange_iteratesCorrectlyInBounds() {
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

        val iteratedDates = mutableListOf<CalendarDay>()
        page.forEachInRange(firstDate.date..lastDate.date) { _, calendarDay ->
            iteratedDates.add(calendarDay)
        }

        assertEquals(
            42,
            iteratedDates.count()
        )
        iteratedDates.forEach {
            assertTrue((firstDate.date..lastDate.date).contains(it.date))
        }
    }

    @Test
    fun forEachInRange_iteratesCorrectlyOutOfBounds() {
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

        val iteratedDates = mutableListOf<CalendarDay>()
        page.forEachInRange(
            firstDate.date..lastDate.date.plus(10, DateTimeUnit.DAY)
        ) { _, calendarDay ->
            iteratedDates.add(calendarDay)
        }

        assertEquals(
            42,
            iteratedDates.count()
        )
        iteratedDates.forEach {
            assertTrue((firstDate.date..lastDate.date).contains(it.date))
        }
    }

    @Test
    fun getFlatDetailsFor_nullWhenOutOfRange() {
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
        assertNull(page.getFlatDetailsFor(LocalDate(2022, Month.MAY, 7)))
        // Check for one after end
        assertNull(page.getFlatDetailsFor(LocalDate(2022, Month.JUNE, 19)))
    }

    @Test
    fun getFlatDetailsFor_returnsWhenInBounds() {
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
        page.getFlatDetailsFor(LocalDate(2022, Month.MAY, 8)).let {
            assertEquals(0, it?.first)
            assertEquals(
                firstDate,
                it?.second
            )
        }
        // Check last date
        page.getFlatDetailsFor(LocalDate(2022, Month.JUNE, 18)).let {
            assertEquals(41, it?.first)
            assertEquals(
                lastDate,
                it?.second
            )
        }
    }
}
