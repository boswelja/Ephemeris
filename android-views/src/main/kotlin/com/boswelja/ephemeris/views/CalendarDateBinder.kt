package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay

/**
 * An interface used to tell the calendar adapter how to create and bind your date cell ViewHolder.
 */
public interface CalendarDateBinder <T : RecyclerView.ViewHolder> {
    /**
     * Called when Ephemeris needs a new ViewHolder for a date cell.
     */
    public fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): T

    /**
     * Called by Ephemeris when data should be displayed in the given cell.
     */
    public fun onBindView(viewHolder: T, calendarDay: CalendarDay)
}
