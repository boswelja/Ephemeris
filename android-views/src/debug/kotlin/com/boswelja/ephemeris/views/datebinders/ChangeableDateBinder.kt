package com.boswelja.ephemeris.views.datebinders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.views.CalendarDateBinder
import com.boswelja.ephemeris.views.databinding.BasicDateCellBinding

public class ChangeableDateBinder(
    private val getBackgroundColor: () -> Int
) : CalendarDateBinder<BasicDateViewHolder> {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BasicDateViewHolder {
        return BasicDateViewHolder(BasicDateCellBinding.inflate(inflater, parent, false))
    }

    override fun onBindView(viewHolder: BasicDateViewHolder, calendarDay: CalendarDay) {
        viewHolder.binding.root.setBackgroundColor(getBackgroundColor())
        viewHolder.binding.dateText.apply {
            text = calendarDay.date.dayOfMonth.toString()
            isEnabled = calendarDay.isFocusedDate
        }
    }
}

public class ChangeableDateViewHolder(
    public val binding: BasicDateCellBinding
) : RecyclerView.ViewHolder(binding.root)
