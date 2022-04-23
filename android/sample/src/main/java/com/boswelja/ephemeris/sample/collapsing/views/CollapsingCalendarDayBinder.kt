package com.boswelja.ephemeris.sample.collapsing.views

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.databinding.DayBinding
import com.boswelja.ephemeris.views.CalendarDateBinder
import com.google.android.material.R
import com.google.android.material.color.MaterialColors

class CollapsingCalendarDayBinder : CalendarDateBinder<CalendarDateViewHolder> {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): CalendarDateViewHolder {
        val view = DayBinding.inflate(inflater, parent, false)
        return CalendarDateViewHolder(view)
    }

    override fun onBindView(viewHolder: CalendarDateViewHolder, calendarDay: CalendarDay) {
        viewHolder.onBind(calendarDay)
    }
}

class CalendarDateViewHolder(private val binding: DayBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(calendarDay: CalendarDay) {
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
