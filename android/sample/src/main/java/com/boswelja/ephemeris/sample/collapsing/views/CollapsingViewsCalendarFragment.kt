package com.boswelja.ephemeris.sample.collapsing.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.collapsing.CollapsingCalendarViewModel
import com.boswelja.ephemeris.sample.databinding.FragmentViewsCalendarBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

class CollapsingViewsCalendarFragment : Fragment(R.layout.fragment_views_calendar) {

    private val binding by viewBinding(FragmentViewsCalendarBinding::bind)
    private val viewModel by viewModels<CollapsingCalendarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchView.setOnClickListener {
            viewModel.toggleCalendarExpanded()
        }

        setupCalendar()
        setupHeader()
    }

    private fun setupCalendar() {
        binding.switchCalendar.initCalendar(
            pageSource = CalendarMonthPageSource(DayOfWeek.SUNDAY),
            dayBinder = CalendarDayBinder()
        )
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.calendarPageSource.collectLatest {
                    binding.switchCalendar.pageSource = it
                }
            }
        }
        binding.switchCalendar.setOnDisplayedDateRangeChangeListener {
            viewModel.handlePageChanged(it)
        }
    }

    private fun setupHeader() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.headerText.collectLatest {
                    binding.headerText.text = it
                }
            }
        }
    }
}
