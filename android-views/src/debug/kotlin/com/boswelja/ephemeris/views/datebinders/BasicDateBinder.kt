package com.boswelja.ephemeris.views.datebinders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.views.databinding.BasicDateCellBinding
import com.boswelja.ephemeris.views.recycling.RecyclingAdapter

public class BasicDateBinder : RecyclingAdapter<BasicDateCellBinding, CalendarDay> {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): BasicDateCellBinding {
        return BasicDateCellBinding.inflate(inflater, parent, false)
    }

    override fun onBindView(binding: BasicDateCellBinding, calendarDay: CalendarDay) {
        binding.dateText.apply {
            text = calendarDay.date.dayOfMonth.toString()
            isEnabled = calendarDay.isFocusedDate
        }
    }
}

public class BasicDateViewHolder(
    public val binding: BasicDateCellBinding
) : RecyclerView.ViewHolder(binding.root)
