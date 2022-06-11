package com.boswelja.ephemeris.sample.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.boswelja.ephemeris.sample.databinding.MainListHeaderBinding
import com.boswelja.ephemeris.sample.databinding.MainListSampleBinding

class SampleAdapter(
    private val onNavigate: (Int) -> Unit
) : ListAdapter<MainItem, MainItemViewHolder>(MainItemDiffer) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Header -> TYPE_HEADER
            is Sample -> TYPE_SAMPLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(MainListHeaderBinding.inflate(inflater, parent, false))
            TYPE_SAMPLE -> SampleViewHolder(MainListSampleBinding.inflate(inflater, parent, false))
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onNavigate)
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_SAMPLE = 1
    }
}

abstract class MainItemViewHolder(binding: ViewBinding) : ViewHolder(binding.root) {
    abstract fun bind(item: MainItem, onNavigate: (Int) -> Unit)
}

class HeaderViewHolder(
    private val binding: MainListHeaderBinding
) : MainItemViewHolder(binding) {
    override fun bind(item: MainItem, onNavigate: (Int) -> Unit) {
        require(item is Header)
        binding.root.text = item.text
    }
}

class SampleViewHolder(
    private val binding: MainListSampleBinding
) : MainItemViewHolder(binding) {
    override fun bind(item: MainItem, onNavigate: (Int) -> Unit) {
        require(item is Sample)
        binding.root.apply {
            setOnClickListener { onNavigate(item.navAction) }
            text = item.text
        }
    }
}

object MainItemDiffer : DiffUtil.ItemCallback<MainItem>() {
    override fun areItemsTheSame(oldItem: MainItem, newItem: MainItem): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: MainItem, newItem: MainItem): Boolean = oldItem == newItem
}