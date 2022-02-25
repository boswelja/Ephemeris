package com.boswelja.ephemeris.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.compose.EphemerisCalendar
import com.boswelja.ephemeris.compose.rememberCalendarState
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.WeekdayFocusMode
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme
import com.boswelja.ephemeris.views.CalendarDateBinder
import kotlinx.datetime.DayOfWeek

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val calendarView = findViewById<EphemerisCalendarView>(R.id.calendar)
//        calendarView.dayBinder = CalendarDayBinder()
//        calendarView.firstDayOfWeek = DayOfWeek.SUNDAY
        setContent {
            EphemerisTheme {
                val calendarState = rememberCalendarState(
                    calendarPageLoader = CalendarMonthPageLoader(
                        DayOfWeek.SUNDAY,
                        WeekdayFocusMode
                    )
                )
                EphemerisCalendar(calendarState = calendarState) { dayState ->
                    Text(
                        text = dayState.date.dayOfMonth.toString(),
                        color = if (dayState.isFocusedDate) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
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
