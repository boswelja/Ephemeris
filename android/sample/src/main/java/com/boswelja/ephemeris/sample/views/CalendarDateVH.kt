package com.boswelja.ephemeris.sample.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.DisplayDate
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

    override fun onBindView(viewHolder: CalendarDateViewHolder, displayDate: DisplayDate) {
        viewHolder.onBind(displayDate)
    }
}

class CalendarDateViewHolder(private val binding: DayBinding) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(displayDate: DisplayDate) {
        binding.dayNum.apply {
            text = displayDate.date.dayOfMonth.toString()
            setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (displayDate.isFocusedDate) R.color.teal_200 else R.color.purple_200)
            )
        }
    }
}