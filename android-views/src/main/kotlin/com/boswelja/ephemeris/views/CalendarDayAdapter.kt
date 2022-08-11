package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.boswelja.ephemeris.core.model.CalendarDay

public interface CalendarDayAdapter<T: ViewBinding> {
    public fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): T

    public fun onBindView(view: T, calendarDay: CalendarDay)
}
