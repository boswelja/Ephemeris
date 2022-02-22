package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.DisplayDate

interface CalendarDateBinder <T : RecyclerView.ViewHolder> {
    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): T
    fun onBindView(viewHolder: T, displayDate: DisplayDate)
}
