package com.boswelja.ephemeris.sample.custompagesource.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.custompagesource.CustomCalendarPageSource
import com.boswelja.ephemeris.sample.databinding.FragmentCustomSourceCalendarBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class CustomSourceViewsCalendarFragment : Fragment(R.layout.fragment_custom_source_calendar) {

    private val binding by viewBinding(FragmentCustomSourceCalendarBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        binding.switchCalendar.initCalendar(
            pageSource = CustomCalendarPageSource(),
            dayBinder = CustomSourceCalendarDayBinder()
        )
    }
}
