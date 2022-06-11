package com.boswelja.ephemeris.sample.collapsing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CollapsingCalendarViewModel : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    private val _calendarExpanded = MutableStateFlow(true)
    private val _headerText = MutableStateFlow("")

    /**
     * Flow whether the calendar should be expanded.
     */
    val calendarExpanded: StateFlow<Boolean> = _calendarExpanded

    /**
     * Flow the text to be displayed in the header.
     */
    val headerText: StateFlow<String> = _headerText

    /**
     * Flows the calendar's current page source.
     */
    val calendarPageSource: StateFlow<CalendarPageSource> = calendarExpanded
        .map { expanded ->
            if (expanded) {
                CalendarMonthPageSource(DayOfWeek.SUNDAY)
            } else {
                CalendarWeekPageSource(DayOfWeek.SUNDAY)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CalendarMonthPageSource(DayOfWeek.SUNDAY)
        )

    /**
     * Toggle the value of [calendarExpanded].
     */
    fun toggleCalendarExpanded() = viewModelScope.launch {
        _calendarExpanded.emit(!_calendarExpanded.value)
    }

    fun handlePageChanged(displayedDateRange: ClosedRange<LocalDate>) {
        viewModelScope.launch {
            val headerText = "${displayedDateRange.start.toJavaLocalDate().format(dateFormatter)} - ${displayedDateRange.endInclusive.toJavaLocalDate().format(dateFormatter)}"
            _headerText.emit(headerText)
        }
    }
}
