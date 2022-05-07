package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.datetime.startOfWeek
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.model.calendarPage
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.todayAt

/**
 * An implementation of [CalendarPageSource] that loads one week per page.
 */
public class CalendarWeekPageSource(
    firstDayOfWeek: DayOfWeek,
    startDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault()),
    private val focusMode: FocusMode = FocusMode.WEEKDAYS
) : CalendarPageSource {

    private val startDate = startDate.startOfWeek(firstDayOfWeek)

    private val daysInWeek = DayOfWeek.values().size
    private val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    override fun loadPageData(
        page: Int
    ): CalendarPage {
        val startOfWeek = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
        return calendarPage {
            row {
                days(daysInWeek) { index ->
                    val date = startOfWeek.plus(index, DateTimeUnit.DAY)
                    this.date = date
                    focused = when (focusMode) {
                        FocusMode.WEEKDAYS -> !weekends.contains(date.dayOfWeek)
                        FocusMode.ALL -> true
                        FocusMode.NONE -> false
                    }
                }
            }
        }
    }

    override fun getPageFor(date: LocalDate): Int {
        val daysUntil = startDate.daysUntil(date)
        // We need to account for the previous page not being 6 dates away from the start
        return if (daysUntil < 0) {
            (daysUntil / daysInWeek) - 1
        } else {
            daysUntil / daysInWeek
        }
    }

    public enum class FocusMode {
        WEEKDAYS,
        ALL,
        NONE
    }
}
