package com.boswelja.ephemeris.sample.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.CalendarWeekPageLoader
import com.boswelja.ephemeris.core.data.DisplayMonthFocusMode
import com.boswelja.ephemeris.core.data.WeekdayFocusMode
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
            val loader = if (it) {
                CalendarMonthPageLoader(
                    binding.switchCalendar.firstDayOfWeek,
                    DisplayMonthFocusMode
                )
            } else {
                CalendarWeekPageLoader(
                    binding.switchCalendar.firstDayOfWeek,
                    WeekdayFocusMode
                )
            }
            binding.switchCalendar.pagingDataSource = loader
        }.launchIn(lifecycleScope)
    }
}