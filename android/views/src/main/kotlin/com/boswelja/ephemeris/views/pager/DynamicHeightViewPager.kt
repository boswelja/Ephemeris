package com.boswelja.ephemeris.views.pager

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.viewpager2.widget.ViewPager2
import com.boswelja.ephemeris.views.databinding.LayoutViewpagerBinding

abstract class DynamicHeightViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val heightAnimator = ValueAnimator().apply { interpolator = FastOutSlowInInterpolator() }

    private val root = LayoutViewpagerBinding.inflate(LayoutInflater.from(context), this, true)

    private val heightAdjuster = ViewPagerHeightAdjuster(viewPager, heightAnimator)

    internal val viewPager: ViewPager2
        get() = root.root

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewPager.registerOnPageChangeCallback(heightAdjuster)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewPager.unregisterOnPageChangeCallback(heightAdjuster)
    }

    fun readjustHeight() {
        heightAdjuster.readjustHeight(viewPager.currentItem)
    }
}
