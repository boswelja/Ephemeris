package com.boswelja.ephemeris.views.pager

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

open class InfiniteAnimatingPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteHorizontalPager(context, attrs, defStyleAttr) {

    private val heightAnimator = ValueAnimator().apply {
        interpolator = FastOutSlowInInterpolator()
        addUpdateListener { animator ->
            layoutParams = layoutParams
                .also { lp -> lp.height = animator.animatedValue as Int }
        }
    }

    private val pageChangeListener = object : OnSnapPositionChangeListener {
        override fun onSnapPositionChange(position: Int) {
            remeasureAndAnimateHeight(position)
        }
    }

    init {
        itemAnimator = PageChangeAnimator(heightAnimator)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        registerSnapPositionChangeListener(pageChangeListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterSnapPositionChangeListener(pageChangeListener)
    }

    internal fun remeasureAndAnimateHeight(position: Int) {
        val view = findViewHolderForAdapterPosition(position)?.itemView
        view?.post {
            val wMeasureSpec = MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY)
            val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (height != view.measuredHeight) {
                heightAnimator.apply {
                    setIntValues(height, view.measuredHeight)
                }.also { it.start() }
            }
        }
    }
}

internal class PageChangeAnimator(
    private val heightAnimator: ValueAnimator
) : DefaultItemAnimator() {
    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean = false

    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ): Boolean = false

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        val fromHeight = preInfo.bottom - preInfo.top
        val toHeight = postInfo.bottom - preInfo.top
        heightAnimator.setIntValues(fromHeight, toHeight)
        heightAnimator.start()
        return false
    }
}
