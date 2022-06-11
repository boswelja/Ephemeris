package com.boswelja.ephemeris.sample.dateselection.views

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.databinding.SelectableDayBinding
import com.boswelja.ephemeris.views.CalendarDateBinder
import com.google.android.material.color.MaterialColors
import kotlinx.datetime.LocalDate

class DateSelectionCalendarDayBinder(
    private val getIsSelected: (LocalDate) -> Boolean,
    private val onDateClicked: (LocalDate) -> Unit
) : CalendarDateBinder<DateSelectionViewHolder> {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): DateSelectionViewHolder {
        return DateSelectionViewHolder(
            SelectableDayBinding.inflate(inflater, parent, false),
            getIsSelected,
            onDateClicked
        )
    }

    override fun onBindView(viewHolder: DateSelectionViewHolder, calendarDay: CalendarDay) {
        viewHolder.onBind(calendarDay)
    }
}

class DateSelectionViewHolder(
    private val binding: SelectableDayBinding,
    private val getIsSelected: (LocalDate) -> Boolean,
    private val onDateClicked: (LocalDate) -> Unit
) : ViewHolder(binding.root) {
    fun onBind(day: CalendarDay) {
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
