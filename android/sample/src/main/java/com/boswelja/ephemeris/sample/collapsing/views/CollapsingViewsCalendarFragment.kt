package com.boswelja.ephemeris.sample.collapsing.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.collapsing.CollapsingCalendarViewModel
import com.boswelja.ephemeris.sample.databinding.FragmentViewsCalendarBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CollapsingViewsCalendarFragment : Fragment(R.layout.fragment_views_calendar) {

    private val binding by viewBinding(FragmentViewsCalendarBinding::bind)
    private val vm by viewModels<CollapsingCalendarViewModel>()
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchView.setOnClickListener {
            vm.toggleView()
        }

        binding.switchCalendar.initCalendar(
            pageSource = CalendarWeekPageSource(
                firstDayOfWeek = DayOfWeek.SUNDAY,
                focusMode = CalendarWeekPageSource.FocusMode.WEEKDAYS
            ),
            dayBinder = CalendarDayBinder()
        )

        lifecycleScope.launch {
            binding.switchCalendar.displayedDateRange.collectLatest {
                updateHeader(it)
            }
        }
        observeCalendarState()
    }

    private fun observeCalendarState() {
        vm.toggle.onEach {
            val buttonText: String = if (it) {
                binding.switchCalendar.pageSource = CalendarMonthPageSource(
                    firstDayOfWeek = DayOfWeek.SUNDAY,
                    focusMode = CalendarMonthPageSource.FocusMode.MONTH
                )
                getString(R.string.toggle_view, getString(R.string.week))
            } else {
                binding.switchCalendar.pageSource = CalendarWeekPageSource(
                    firstDayOfWeek = DayOfWeek.SUNDAY,
                    focusMode = CalendarWeekPageSource.FocusMode.WEEKDAYS
                )
                getString(R.string.toggle_view, getString(R.string.month))
            }
            binding.switchView.text = buttonText
        }.launchIn(lifecycleScope)
    }

    private fun updateHeader(dateRange: ClosedRange<LocalDate>) {
        val headerText = "${dateRange.start.toJavaLocalDate().format(dateFormatter)} - ${dateRange.endInclusive.toJavaLocalDate().format(dateFormatter)}"
        binding.headerText.text = headerText
    }
}
