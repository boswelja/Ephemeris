package com.boswelja.ephemeris.views

import android.animation.ValueAnimator
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * An implementation of [ViewPager2.OnPageChangeCallback] that animates height changes when the
 * displayed page changes.
 */
class ViewPagerHeightAdjuster private constructor(
    private val viewPager: ViewPager2
) : ViewPager2.OnPageChangeCallback() {

    private val heightAnimator = ValueAnimator().apply {
        addUpdateListener { animator ->
            viewPager.layoutParams = viewPager.layoutParams
                .also { lp -> lp.height = animator.animatedValue as Int }
        }
        interpolator = FastOutSlowInInterpolator()
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
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

    companion object {
        fun attachTo(viewPager: ViewPager2): ViewPagerHeightAdjuster {
            val heightAdjuster = ViewPagerHeightAdjuster(viewPager)
            viewPager.registerOnPageChangeCallback(heightAdjuster)
            return heightAdjuster
        }
    }
}
