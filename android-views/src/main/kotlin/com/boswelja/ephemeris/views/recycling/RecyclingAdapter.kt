package com.boswelja.ephemeris.views.recycling

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

public interface RecyclingAdapter<V, T> {
    public fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): V

    public fun onBindView(view: V, data: T)
}
