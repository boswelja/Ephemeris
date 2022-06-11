package com.boswelja.ephemeris.sample.dateselection.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.databinding.FragmentDateSelectionCalendarBinding
import com.boswelja.ephemeris.sample.dateselection.DateSelectionViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateSelectionViewsCalendarFragment : Fragment(R.layout.fragment_date_selection_calendar) {

    private val binding by viewBinding(FragmentDateSelectionCalendarBinding::bind)
    private val viewModel: DateSelectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.calendarView.apply {
            pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY)
            dateBinder = DateSelectionCalendarDayBinder(
                getIsSelected = { it == viewModel.selectedDate.value },
                onDateClicked = { viewModel.selectDate(it) }
            )
            animateHeight = true
        }
        lifecycleScope.launch {
            viewModel.selectedDate
                .collectChanges { old, new ->
                    old?.let { binding.calendarView.notifyDateChanged(it) }
                    binding.calendarView.notifyDateChanged(new)
                    binding.headerText.text = getString(
                        R.string.selected_date,
                        new.toJavaLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    )
                }
        }
    }
}

private suspend fun <T> StateFlow<T>.collectChanges(block: (old: T?, new: T) -> Unit) {
    var oldValue: T? = null
    collect {
        block(oldValue, it)
        oldValue = it
    }
}
