package com.boswelja.ephemeris.sample.dateselection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class DateSelectionViewModel : ViewModel() {
    private val _selectedDate = MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))

    val selectedDate: StateFlow<LocalDate> = _selectedDate

    fun selectDate(newDate: LocalDate) {
        _selectedDate.value = newDate
    }
}
