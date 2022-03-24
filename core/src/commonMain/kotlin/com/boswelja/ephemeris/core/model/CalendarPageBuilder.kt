package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate

public fun calendarPage(init: CalendarPageBuilder.() -> Unit): CalendarPage {
    return CalendarPageBuilder().apply(init).build()
}

public class CalendarPageBuilder internal constructor() {
    private val rows = mutableListOf<CalendarRow>()

    public fun row(init: CalendarRowBuilder.() -> Unit) {
        val row = CalendarRowBuilder().apply(init).build()
        rows.add(row)
    }

    public fun rows(count: Int, init: CalendarRowBuilder.() -> Unit) {
        repeat(count) {
            row(init)
        }
    }

    internal fun build(): CalendarPage {
        require(rows.isNotEmpty())
        return CalendarPage(rows)
    }
}

public class CalendarRowBuilder internal constructor() {
    private val days = mutableListOf<CalendarDay>()

    public fun day(init: CalendarDayBuilder.() -> Unit) {
        val day = CalendarDayBuilder().apply(init).build()
        days.add(day)
    }

    public fun days(count: Int, init: CalendarDayBuilder.() -> Unit) {
        repeat(count) {
            day(init)
        }
    }

    internal fun build(): CalendarRow {
        require(days.isNotEmpty())
        return CalendarRow(days)
    }
}

public class CalendarDayBuilder internal constructor() {
    private var date: LocalDate? = null
    private var focused: Boolean? = null

    public fun date(init: () -> LocalDate) { date = init() }

    public fun focused(init: () -> Boolean) { focused = init() }

    internal fun build(): CalendarDay =
        CalendarDay(
            date!!,
            focused!!
        )
}
