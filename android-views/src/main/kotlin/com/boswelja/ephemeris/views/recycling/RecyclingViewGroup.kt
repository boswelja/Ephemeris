package com.boswelja.ephemeris.views.recycling

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.annotation.CallSuper
import androidx.core.view.children
import androidx.viewbinding.ViewBinding

public abstract class RecyclingViewGroup<V: ViewBinding, T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var currentScrollX: Float = 0f
    private var currentScrollY: Float = 0f
    protected val hasScrolled: Boolean
        get() = (currentScrollX != 0f && currentScrollY != 0f) || scroller.computeScrollOffset()

    protected val scroller: OverScroller = OverScroller(context)
    private val gestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(event: MotionEvent): Boolean {
            return true
        }

        override fun onShowPress(event: MotionEvent) {
        }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return false
        }

        override fun onScroll(
            initialEvent: MotionEvent,
            moveEvent: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            currentScrollX += -distanceX
            currentScrollY += -distanceY
            invalidate()
            return true
        }

        override fun onLongPress(event: MotionEvent) {
        }

        override fun onFling(
            firstEvent: MotionEvent,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            scroller.fling(
                firstEvent.x.toInt(),
                firstEvent.y.toInt(),
                velocityX.toInt(),
                velocityY.toInt(),
                0,
                Int.MAX_VALUE,
                0,
                Int.MAX_VALUE
            )
            postInvalidateOnAnimation()
            return true
        }
    }
    private val gestureDetector = GestureDetector(context, gestureListener)

    private val layoutInflater = LayoutInflater.from(context)

    private val recycledBindingPool = mutableListOf<V>()

    protected lateinit var adapter: RecyclingAdapter<V, T>
        private set

    override fun invalidate() {
        // We also want to clear any recycled bindings when things are invalidated
        recycledBindingPool.clear()
        super.invalidate()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return ev.actionMasked == MotionEvent.ACTION_MOVE
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun computeScroll() {
        val scrollX = currentScrollX.toInt()
        currentScrollX = 0f
        children.forEach {
            it.layout(
                it.left + scrollX,
                it.top,
                it.right + scrollX,
                it.bottom
            )
        }
    }

    @CallSuper
    public open fun setAdapter(adapter: RecyclingAdapter<V, T>) {
        this.adapter = adapter
        invalidate()
    }

    protected fun getOrCreateBinding(widthMeasureSpec: Int, heightMeasureSpec: Int): V {
        require(::adapter.isInitialized) { "You must set an adapter before calling getOrCreateBinding" }
        val binding = recycledBindingPool.removeLastOrNull() ?: let {
            val newBinding = adapter.onCreateView(layoutInflater, this)
            newBinding.root.measure(widthMeasureSpec, heightMeasureSpec)
            newBinding
        }
        addViewInLayout(
            binding.root,
            -1,
            binding.root.layoutParams ?: generateDefaultLayoutParams(),
            true
        )
        return binding
    }

    protected fun removeAndRecycleBinding(binding: V) {
        removeViewInLayout(binding.root)
        recycledBindingPool.add(binding)
    }

    protected fun measureRecycledBinding(widthMeasureSpec: Int, heightMeasureSpec: Int): Size {
        // Measure a recycled cell view
        // TODO if we create a new view, scrap it
        val binding = recycledBindingPool.firstOrNull() ?: adapter.onCreateView(layoutInflater, this)
        binding.root.measure(widthMeasureSpec, heightMeasureSpec)
        return Size(binding.root.measuredWidth, binding.root.measuredHeight)
    }

}
