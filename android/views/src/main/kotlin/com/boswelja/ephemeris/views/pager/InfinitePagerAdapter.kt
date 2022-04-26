package com.boswelja.ephemeris.views.pager

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

public abstract class InfinitePagerAdapter<T: ViewHolder> : Adapter<T>() {

    public abstract fun onBindHolder(holder: T, page: Int)

    override fun getItemCount(): Int = MAX_PAGES

    override fun getItemId(position: Int): Long = position.toLong()

    final override fun onBindViewHolder(holder: T, position: Int) {
        onBindHolder(holder, positionToPage(position))
    }

    internal fun positionToPage(position: Int): Int {
        return position - (MAX_PAGES / 2)
    }

    /**
     * Maps a pager page to the underlying RecyclerView position
     */
    internal fun pageToPosition(page: Int): Int {
        return page + (Int.MAX_VALUE / 2)
    }

    private companion object {
        private const val MAX_PAGES = Int.MAX_VALUE
    }
}
