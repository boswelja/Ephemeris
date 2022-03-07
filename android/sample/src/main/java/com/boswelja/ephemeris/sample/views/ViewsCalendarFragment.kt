package com.boswelja.ephemeris.sample.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.boswelja.ephemeris.core.data.*
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.databinding.FragmentViewsCalendarBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ViewsCalendarFragment : Fragment(R.layout.fragment_views_calendar) {

    private val binding by viewBinding(FragmentViewsCalendarBinding::bind)
    private val vm by viewModels<CalendarViewVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchView.setOnClickListener {
            vm.toggleView()
        }

        binding.monthCalendar.pagingDataSource = CalendarMonthPageLoader(
            binding.monthCalendar.firstDayOfWeek,
            DisplayMonthFocusMode
        )

        binding.weekCalendar.pagingDataSource = CalendarWeekPageLoader(
            binding.monthCalendar.firstDayOfWeek,
            WeekdayFocusMode
        )

        with(CalendarDayBinder()) {
            binding.monthCalendar.dayBinder = this
            binding.weekCalendar.dayBinder = this
            binding.switchCalendar.dayBinder = this
        }

        observeCalendarState()
    }

    private fun observeCalendarState() {
        vm.toggle.onEach {
            val loader: CalendarPageLoader
            val buttonText: String
            if (it) {
                loader = CalendarMonthPageLoader(
                    binding.switchCalendar.firstDayOfWeek,
                    DisplayMonthFocusMode
                )
                buttonText = getString(R.string.toggle_view, getString(R.string.week))
            } else {
                loader = CalendarWeekPageLoader(
                    binding.switchCalendar.firstDayOfWeek,
                    WeekdayFocusMode
                )
                buttonText = getString(R.string.toggle_view, getString(R.string.month))
            }
            binding.switchCalendar.pagingDataSource = loader
            binding.switchView.text = buttonText
        }.launchIn(lifecycleScope)
    }
}