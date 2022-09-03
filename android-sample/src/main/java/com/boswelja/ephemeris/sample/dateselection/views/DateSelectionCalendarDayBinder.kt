package com.boswelja.ephemeris.sample.dateselection.views

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.databinding.SelectableDayBinding
import com.boswelja.ephemeris.views.recycling.RecyclingAdapter
import com.google.android.material.color.MaterialColors
import kotlinx.datetime.LocalDate

class DateSelectionCalendarDayBinder(
    private val getIsSelected: (LocalDate) -> Boolean,
    private val onDateClicked: (LocalDate) -> Unit
) : RecyclingAdapter<SelectableDayBinding, CalendarDay> {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): SelectableDayBinding {
        return SelectableDayBinding.inflate(inflater, parent, false)
    }

    override fun onBindView(binding: SelectableDayBinding, day: CalendarDay) {
        val isSelected = getIsSelected(day.date)

        binding.dayNum.apply {
            setTypeface(
                null,
                if (day.isFocusedDate) Typeface.BOLD
                else Typeface.NORMAL
            )
            text = day.date.dayOfMonth.toString()
        }

        binding.root.apply {
            isEnabled = day.isFocusedDate
            setCardBackgroundColor(
                if (isSelected) {
                    MaterialColors.getColor(this, com.google.android.material.R.attr.colorPrimaryContainer)
                } else {
                    MaterialColors.getColor(this, com.google.android.material.R.attr.colorSurface)
                }
            )
            setOnClickListener {
                onDateClicked(day.date)
            }
        }
    }
}
