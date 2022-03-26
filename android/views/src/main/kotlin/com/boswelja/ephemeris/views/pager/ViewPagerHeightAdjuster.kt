package com.boswelja.ephemeris.views.pager

import android.animation.ValueAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * An implementation of [ViewPager2.OnPageChangeCallback] that animates height changes when the
 * displayed page changes.
 */
internal class ViewPagerHeightAdjuster(
    private val viewPager: ViewPager2,
    private val heightAnimator: ValueAnimator
) : ViewPager2.OnPageChangeCallback() {

    init {
        heightAnimator.apply {
            addUpdateListener { animator ->
                viewPager.layoutParams = viewPager.layoutParams
                    .also { lp -> lp.height = animator.animatedValue as Int }
            }
        }
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        readjustHeight(position)
    }

    fun readjustHeight(position: Int) {
        val view = (viewPager.getChildAt(0) as RecyclerView).findViewHolderForAdapterPosition(position)?.itemView
        view?.post {
            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (viewPager.height != view.measuredHeight) {
                heightAnimator.apply {
                    setIntValues(viewPager.height, view.measuredHeight)
                }.also { it.start() }
            }
        }
    }
}
