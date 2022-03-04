package com.boswelja.ephemeris.sample.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.databinding.FragmentViewsCalendarBinding
import com.boswelja.ephemeris.views.CalendarDateBinder
import kotlinx.datetime.DayOfWeek

class ViewsCalendarFragment : Fragment() {

    private var binding: FragmentViewsCalendarBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewsCalendarBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.calendar?.apply {
            dayBinder = CalendarDayBinder()
            firstDayOfWeek = DayOfWeek.SUNDAY
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

class CalendarDayBinder : CalendarDateBinder<CalendarDateViewHolder> {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): CalendarDateViewHolder {
        val view = inflater.inflate(R.layout.day, parent, false)
        return CalendarDateViewHolder(view)
    }

    override fun onBindView(viewHolder: CalendarDateViewHolder, displayDate: DisplayDate) {
        val textColor = viewHolder.itemView.context.getColor(
            if (displayDate.isFocusedDate) R.color.purple_500 else android.R.color.black
        )
        viewHolder.dateNum.apply {
            text = displayDate.date.dayOfMonth.toString()
            setTextColor(textColor)
        }
    }
}

class CalendarDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateNum = itemView.findViewById<TextView>(R.id.day_num)
}
