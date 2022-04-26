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

    private var snapPositionChangeListener: ((page: Int) -> Unit)? = null
    private val snapHelper = PagerSnapHelper()

    /**
     * The current page that the pager is snapped to.
     */
    public var currentPage: Int = 0

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
        super.scrollToPosition(pageToPosition(currentPage))
    }

    /**
     * Called when the pager is snapping to a new page.
     */
    @CallSuper
    public open fun onPageSnapping(page: Int) {
        snapPositionChangeListener?.invoke(page)
    }

    public fun setOnSnapPositionChangeListener(listener: (page: Int) -> Unit) {
        snapPositionChangeListener = listener
    }

    @CallSuper
    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (scrollState != SCROLL_STATE_DRAGGING) {
            val snapTarget = snapHelper.findTargetSnapPosition(layoutManager, dx, dy)
            if (snapTarget != NO_POSITION) {
                val snapPage = positionToPage(snapTarget)
                maybeNotifySnapPositionChange(snapPage)
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        require(adapter is InfinitePagerAdapter<*>) { "$adapter is not an InfinitePagerAdapter!" }
        super.setAdapter(adapter)
    }

    override fun findViewHolderForAdapterPosition(position: Int): ViewHolder? {
        return super.findViewHolderForAdapterPosition(pageToPosition(position))
    }

    override fun findViewHolderForLayoutPosition(position: Int): ViewHolder? {
        return super.findViewHolderForLayoutPosition(pageToPosition(position))
    }

    final override fun smoothScrollToPosition(position: Int) {
        super.smoothScrollToPosition(pageToPosition(position))
    }

    final override fun scrollToPosition(position: Int) {
        super.scrollToPosition(pageToPosition(position))
        // Since scrollToPosition doesn't actually trigger a scroll state change, we need to manually
        // notify listeners. Maybe there's a better way of handling this?
        maybeNotifySnapPositionChange(position)
    }

    private fun maybeNotifySnapPositionChange(snapPosition: Int) {
        val snapPositionChanged = currentPage != snapPosition
        if (snapPositionChanged) {
            currentPage = snapPosition
            onPageSnapping(snapPosition)
        }
    }

    /**
     * Maps a position from the underlying RecyclerView to a pager page.
     */
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
