package com.boswelja.ephemeris.views.pager

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

internal fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    return recyclerView.layoutManager?.let { layoutManager ->
        findSnapView(layoutManager)?.let { snapView -> layoutManager.getPosition(snapView) }
    } ?: RecyclerView.NO_POSITION
}
