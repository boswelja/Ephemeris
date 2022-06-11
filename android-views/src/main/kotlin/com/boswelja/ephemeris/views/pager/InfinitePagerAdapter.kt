package com.boswelja.ephemeris.views.pager

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

internal abstract class InfinitePagerAdapter<T: ViewHolder> : Adapter<T>() {

    override fun getItemId(position: Int): Long = position.toLong()

    abstract fun positionToPage(position: Int): Int

    /**
     * Maps a pager page to the underlying RecyclerView position
     */
    abstract fun pageToPosition(page: Int): Int
}
