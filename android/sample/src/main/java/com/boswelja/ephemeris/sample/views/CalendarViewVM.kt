package com.boswelja.ephemeris.sample.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CalendarViewVM : ViewModel() {

    private val _toggleView = MutableStateFlow(false)
    val toggle: Flow<Boolean> = _toggleView

    /**
     * Invert the current state of calendar view
     * true maps to [com.boswelja.ephemeris.core.data.CalendarMonthPageSource] with [com.boswelja.ephemeris.core.data.DisplayMonthFocusMode]
     * false maps to [com.boswelja.ephemeris.core.data.CalendarWeekPageSource] with [com.boswelja.ephemeris.core.data.WeekdayFocusMode]
     */
    fun toggleView() = viewModelScope.launch {
        _toggleView.emit(!_toggleView.value)
    }
}