package com.boswelja.ephemeris.views.recycling

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding

public abstract class RecyclingViewGroup<V: ViewBinding, T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val layoutInflater = LayoutInflater.from(context)

    private val recycledBindingPool = mutableListOf<V>()

    protected lateinit var adapter: RecyclingAdapter<V, T>
        private set

    @CallSuper
    public open fun setAdapter(adapter: RecyclingAdapter<V, T>) {
        this.adapter = adapter
        invalidate()
    }

    override fun invalidate() {
        // We also want to clear any recycled bindings when things are invalidated
        recycledBindingPool.clear()
        super.invalidate()
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
