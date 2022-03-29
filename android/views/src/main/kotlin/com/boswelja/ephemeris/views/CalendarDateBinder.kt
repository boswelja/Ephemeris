package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay

public interface CalendarDateBinder <T : RecyclerView.ViewHolder> {
    public fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): T
    public fun onBindView(viewHolder: T, calendarDay: CalendarDay)
}
