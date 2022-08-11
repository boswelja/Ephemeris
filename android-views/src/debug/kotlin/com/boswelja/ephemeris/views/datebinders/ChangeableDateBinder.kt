package com.boswelja.ephemeris.views.datebinders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.views.CalendarDayAdapter
import com.boswelja.ephemeris.views.databinding.BasicDateCellBinding

public class ChangeableDateBinder(
    private val getBackgroundColor: () -> Int
) : CalendarDayAdapter<BasicDateCellBinding> {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): BasicDateCellBinding {
        return BasicDateCellBinding.inflate(inflater, parent, false)
    }

    override fun onBindView(binding: BasicDateCellBinding, calendarDay: CalendarDay) {
        binding.root.setBackgroundColor(getBackgroundColor())
        binding.dateText.apply {
            text = calendarDay.date.dayOfMonth.toString()
            isEnabled = calendarDay.isFocusedDate
        }
    }
}

public class ChangeableDateViewHolder(
    public val binding: BasicDateCellBinding
) : RecyclerView.ViewHolder(binding.root)
