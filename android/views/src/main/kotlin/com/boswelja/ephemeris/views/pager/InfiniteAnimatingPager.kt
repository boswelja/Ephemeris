package com.boswelja.ephemeris.views.pager

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

/**
 * An implementation of [InfiniteHorizontalPager] that automatically animates page height changes.
 */
public open class InfiniteAnimatingPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mainPager = InfiniteHorizontalPager(context)
    private var secondaryPager = InfiniteHorizontalPager(context)

    private val heightAnimator = ValueAnimator().apply {
        addUpdateListener { animator ->
            val animatedHeight = animator.animatedValue as Int
            setHeight(animatedHeight)
        }
    }

    /**
     * Controls whether animations will play when the pager height changes. If false, the pager will
     * jump to the new height rather than animate smoothly.
     */
    public var animateHeight: Boolean = true

    /**
     * The current page that the pager is snapped to.
     */
    public val currentPage: Int
        get() = mainPager.currentPage

    init {
        layoutTransition = LayoutTransition()
    }

    private fun switchPagers(newAdapter: InfinitePagerAdapter<*>) {
        synchronized(this) {
            val newPager = secondaryPager
            newPager.apply {
                adapter = newAdapter
            }
            newPager.setOnSnapPositionChangeListener { onPageSnapping(it) }
            newPager.scrollToPosition(mainPager.currentPage)

            removeViewInLayout(mainPager)
            addView(newPager)

            secondaryPager = mainPager
            mainPager = newPager
        }
    }

    public fun smoothScrollToPage(page: Int) {
        mainPager.smoothScrollToPosition(page)
    }

    public fun scrollToPage(page: Int) {
        mainPager.scrollToPosition(page)
    }

    /**
     * Called when the pager is snapping to a new page.
     */
    @CallSuper
    public open fun onPageSnapping(page: Int) {
        remeasureAndAnimateHeight(page)
    }

    public fun setAdapter(adapter: InfinitePagerAdapter<*>) {
        switchPagers(adapter)
    }

    private fun remeasureAndAnimateHeight(position: Int) {
        val viewHolder = mainPager.findViewHolderForAdapterPosition(position)
        viewHolder?.itemView?.post {
            val wMeasureSpec = MeasureSpec.makeMeasureSpec(viewHolder.itemView.width, MeasureSpec.EXACTLY)
            val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            viewHolder.itemView.measure(wMeasureSpec, hMeasureSpec)
            val targetHeight = viewHolder.itemView.measuredHeight
            animateHeight(
                viewHolder,
                targetHeight
            )
        }
    }

    private fun animateHeight(
        viewHolder: RecyclerView.ViewHolder,
        toHeight: Int,
        fromHeight: Int = height
    ) {
        if (fromHeight == toHeight) return

        val page = mainPager.positionToPage(viewHolder.bindingAdapterPosition)
        if (animateHeight && page == mainPager.currentPage) {
            heightAnimator.run {
                val currentHeight = if (isRunning) animatedValue as Int else fromHeight
                setIntValues(currentHeight, toHeight)
                start()
            }
        } else {
            setHeight(toHeight)
        }
    }

    private fun setHeight(targetHeight: Int) {
        for (index in 0 until mainPager.childCount) {
            mainPager.getChildAt(index).apply {
                layoutParams = layoutParams.also {
                    it.height = targetHeight
                }
            }
        }
    }
}
