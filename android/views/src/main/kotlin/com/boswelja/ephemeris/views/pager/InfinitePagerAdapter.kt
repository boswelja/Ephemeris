package com.boswelja.ephemeris.views.pager

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class InfinitePagerAdapter<T: ViewHolder> : Adapter<T>() {

    init {
        setHasStableIds(true)
    }

    abstract fun onBindHolder(holder: T, page: Int)

    override fun getItemCount(): Int = MAX_PAGES

    override fun getItemId(position: Int): Long = position.toLong()

    final override fun setHasStableIds(hasStableIds: Boolean) = super.setHasStableIds(hasStableIds)

    final override fun onBindViewHolder(holder: T, position: Int) {
        onBindHolder(holder, positionToPage(position))
    }

    private fun positionToPage(position: Int): Int {
        return position - (MAX_PAGES / 2)
    }

    companion object {
        private const val MAX_PAGES = Int.MAX_VALUE
    }
}
