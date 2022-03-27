package com.boswelja.ephemeris.sample.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.databinding.DayBinding
import com.boswelja.ephemeris.views.CalendarDateBinder

class CalendarDayBinder : CalendarDateBinder<CalendarDateViewHolder> {
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
                ContextCompat.getColor(
                    binding.root.context,
                    if (calendarDay.isFocusedDate) {
                        R.color.purple_500
                    } else {
                        R.color.black
                    }
                )
            )
        }
    }
}