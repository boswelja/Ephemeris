package com.boswelja.ephemeris.sample.custompagesource

import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.model.calendarPage
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayAt

/**
 * An implementation of [CalendarPageSource] that loads one week per page.
 */
class CustomCalendarPageSource(
    startDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
) : CalendarPageSource {

    private val startDate = startDate.startOfRow(DayOfWeek.MONDAY)

    private val daysInRow = 5
    private val rowCount = 2

    override fun loadPageData(
        page: Int
    ): CalendarPage {
        return calendarPage {
            rows(rowCount) { rowNum ->
                val startOfRow = startDate.plus((page * rowCount) + rowNum, DateTimeUnit.WEEK)
                days(daysInRow) { dayNum ->
                    val date = startOfRow.plus(dayNum, DateTimeUnit.DAY)
                    date { date }
                    focused {
                        when (date.dayOfWeek) {
                            DayOfWeek.MONDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.FRIDAY -> true
                            else -> false
                        }
                    }
                }
            }
        }
    }

    override fun getPageFor(date: LocalDate): Int {
        val daysUntil = startDate.daysUntil(date)
        // We need to account for the previous page not being 6 dates away from the start
        return if (daysUntil < 0) {
            (daysUntil / daysInRow) - 1
        } else {
            daysUntil / daysInRow
        } / rowCount
    }

    private fun DayOfWeek.minusDays(days: Long): DayOfWeek {
        val daysInWeek = DayOfWeek.values().size
        val newNumber = (this.isoDayNumber - days) % daysInWeek
        return if (newNumber > 0) {
            DayOfWeek(newNumber.toInt())
        } else {
            DayOfWeek(newNumber.toInt() + daysInWeek)
        }
    }

    private fun LocalDate.startOfRow(rowStart: DayOfWeek): LocalDate {
        if (dayOfWeek == rowStart) return this
        val offset = dayOfWeek.minusDays(rowStart.value.toLong()).value
        return minus(offset, DateTimeUnit.DAY)
    }
}
