package com.boswelja.ephemeris.sample.custompagesource.views

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.databinding.DayBinding
import com.boswelja.ephemeris.views.CalendarDayAdapter
import com.google.android.material.R
import com.google.android.material.color.MaterialColors

class CustomSourceCalendarDayBinder : CalendarDayAdapter<DayBinding> {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): DayBinding {
        return DayBinding.inflate(inflater, parent, false)
    }

    override fun onBindView(binding: DayBinding, calendarDay: CalendarDay) {
        binding.dayNum.apply {
            text = calendarDay.date.dayOfMonth.toString()
            setTextColor(
                if (calendarDay.isFocusedDate) {
                    MaterialColors.getColor(binding.root, R.attr.colorPrimary)
                } else {
                    MaterialColors.getColor(binding.root, R.attr.colorSecondary)
                }
            )
            setTypeface(
                null,
                if (calendarDay.isFocusedDate) Typeface.BOLD
                else Typeface.NORMAL
            )
        }
    }
}
