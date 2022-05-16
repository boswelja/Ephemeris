package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.datetime.YearMonth
import com.boswelja.ephemeris.core.datetime.endOfWeek
import com.boswelja.ephemeris.core.datetime.plus
import com.boswelja.ephemeris.core.datetime.startOfWeek
import com.boswelja.ephemeris.core.datetime.until
import com.boswelja.ephemeris.core.datetime.yearMonth
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
 * An implementation of [CalendarPageSource] that loads full months. Total rows are dynamic, and
 * will change from month to month. The default range for the calendar is 500 years before and after
 * the given [startYearMonth], and can be overridden by setting [maxPageRange].
 */
public class CalendarMonthPageSource(
    private val firstDayOfWeek: DayOfWeek,
    private val startYearMonth: YearMonth = Clock.System.todayAt(TimeZone.currentSystemDefault()).yearMonth,
    private val focusMode: FocusMode = FocusMode.MONTH,
    override val maxPageRange: IntRange = IntRange(-6000, 6000)
) : CalendarPageSource {
    private val daysInWeek = DayOfWeek.values().size
    private val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    override val hasOverlappingDates: Boolean = true

    override fun loadPageData(page: Int): CalendarPage {
        val month = startYearMonth.plus(page)
        val firstDisplayedDate = month.startDate.startOfWeek(firstDayOfWeek)
        val lastDisplayedDate = month.endDate.endOfWeek(firstDayOfWeek)
        return calendarPage {
            rows(weeksInRange(firstDisplayedDate, lastDisplayedDate)) { weekIndex ->
                val startDate = firstDisplayedDate.plus(weekIndex, DateTimeUnit.WEEK)
                days(daysInWeek) { dayIndex ->
                    val date = startDate.plus(dayIndex, DateTimeUnit.DAY)
                    this.date = date
                    focused = when (focusMode) {
                        FocusMode.MONTH -> date.yearMonth == month
                        FocusMode.WEEKDAYS -> !weekends.contains(date.dayOfWeek)
                        FocusMode.ALL -> true
                        FocusMode.NONE -> false
                    }
                }
            }
        }
    }

    override fun getPageFor(date: LocalDate): Int {
        return startYearMonth.until(date.yearMonth)
    }

    private fun weeksInRange(startDate: LocalDate, endDate: LocalDate): Int {
        return (startDate.daysUntil(endDate) + 1) / daysInWeek
    }

    public enum class FocusMode {
        MONTH,
        WEEKDAYS,
        ALL,
        NONE
    }
}
