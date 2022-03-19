package com.boswelja.ephemeris.views

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class InfiniteScrollBehaviour(
    private val itemCount: Int,
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(
        recyclerView: RecyclerView, dx: Int, dy: Int
    ) {
        super.onScrolled(recyclerView, dx, dy)
        val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
        val lastItemVisible = layoutManager.findLastVisibleItemPosition()
        if (firstItemVisible == (itemCount - 1) && dx > 0) {
            recyclerView.scrollToPosition(1)
        } else if (lastItemVisible == 0 && dx < 0) {
            recyclerView.scrollToPosition(itemCount - 2)
        }
    }
}