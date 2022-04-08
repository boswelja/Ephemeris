package com.boswelja.ephemeris.views.pager

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * An implementation of [RecyclerView] that provides seemingly infinite left/right paging support.
 * Implementations of this must subclass [InfinitePagerAdapter] for their adapter.
 */
public open class InfiniteHorizontalPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val snapHelper = PagerSnapHelper()

    /**
     * The current page that the pager is snapped to.
     */
    public var currentPage: Int = 0

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
        scrollToPosition(currentPage)
    }

    /**
     * Called when the pager snaps to a new page.
     */
    @CallSuper
    public open fun onPageSnap(page: Int) { }

    @CallSuper
    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        require(adapter is InfinitePagerAdapter<*>) { "$adapter is not an InfinitePagerAdapter!" }
        super.setAdapter(adapter)
    }

    override fun findViewHolderForAdapterPosition(position: Int): ViewHolder? {
        return super.findViewHolderForAdapterPosition(pageToPosition(position))
    }

    final override fun smoothScrollToPosition(position: Int) {
        super.smoothScrollToPosition(pageToPosition(position))
    }

    final override fun scrollToPosition(position: Int) {
        super.scrollToPosition(pageToPosition(position))
    }

    private fun maybeNotifySnapPositionChange() {
        val snapPosition = positionToPage(snapHelper.getSnapPosition(this))
        val snapPositionChanged = currentPage != snapPosition
        if (snapPositionChanged) {
            currentPage = snapPosition
            onPageSnap(snapPosition)
        }
    }

    /**
     * Maps a position from the underlying RecyclerView to a pager page.
     */
    protected fun positionToPage(position: Int): Int {
        return position - (MAX_PAGES / 2)
    }

    /**
     * Maps a pager page to the underlying RecyclerView position
     */
    protected fun pageToPosition(page: Int): Int {
        return page + (Int.MAX_VALUE / 2)
    }

    private companion object {
        private const val MAX_PAGES = Int.MAX_VALUE
    }
}
