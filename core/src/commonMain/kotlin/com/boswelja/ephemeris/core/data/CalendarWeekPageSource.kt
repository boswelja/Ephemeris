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
 * An implementation of [CalendarPageSource] that loads one week per page. The default range for the
 * calendar is 500 years before and after the given [startDate], and can be overridden by setting
 * [maxPageRange].
 */
public class CalendarWeekPageSource(
    firstDayOfWeek: DayOfWeek,
    startDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault()),
    private val focusMode: FocusMode = FocusMode.WEEKDAYS,
    override val maxPageRange: IntRange = IntRange(-26000, 26000)
) : CalendarPageSource {

    private val startDate = startDate.startOfWeek(firstDayOfWeek)

    private val daysInWeek = DayOfWeek.values().size
    private val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    override val hasOverlappingDates: Boolean = false

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
            // If there is remainder，add a pageOffset 1
            val pageOffset = if (daysUntil % daysInWeek != 0) 1 else 0
            (daysUntil / daysInWeek) - pageOffset
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
