package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate

@DslMarker
public annotation class CalendarPageDsl

public fun calendarPage(init: CalendarPageBuilder.() -> Unit): CalendarPage {
    return CalendarPageBuilder().apply(init).build()
}

@CalendarPageDsl
public class CalendarPageBuilder internal constructor() {
    private val rows = mutableListOf<CalendarRow>()

    public fun row(init: CalendarRowBuilder.() -> Unit) {
        val row = CalendarRowBuilder().apply(init).build()
        rows.add(row)
    }

    public fun rows(count: Int, init: CalendarRowBuilder.(Int) -> Unit) {
        repeat(count) {
            val row = CalendarRowBuilder()
                .apply { init(it) }
                .build()
            rows.add(row)
        }
    }

    internal fun build(): CalendarPage {
        require(rows.isNotEmpty())
        return CalendarPage(rows)
    }
}

@CalendarPageDsl
public class CalendarRowBuilder internal constructor() {
    private val days = mutableListOf<CalendarDay>()

    public fun day(init: CalendarDayBuilder.() -> Unit) {
        val day = CalendarDayBuilder().apply(init).build()
        days.add(day)
    }

    public fun days(count: Int, init: CalendarDayBuilder.(Int) -> Unit) {
        repeat(count) {
            val day = CalendarDayBuilder()
                .apply { init(it) }
                .build()
            days.add(day)
        }
    }

    internal fun build(): CalendarRow {
        require(days.isNotEmpty())
        return CalendarRow(days)
    }
}

@CalendarPageDsl
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
