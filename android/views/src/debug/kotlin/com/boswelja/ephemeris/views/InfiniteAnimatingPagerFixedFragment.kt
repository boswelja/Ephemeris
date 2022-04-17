package com.boswelja.ephemeris.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.views.databinding.FragmentAnimatingPagerFixedBinding
import com.boswelja.ephemeris.views.pager.InfiniteAnimatingPager

public class InfiniteAnimatingPagerFixedFragment : Fragment() {
    public lateinit var pager: InfiniteAnimatingPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAnimatingPagerFixedBinding.inflate(inflater, container, false)
        pager = binding.pagerView
        return binding.root
    }
}
