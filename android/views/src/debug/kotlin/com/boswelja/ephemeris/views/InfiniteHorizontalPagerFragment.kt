package com.boswelja.ephemeris.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.views.databinding.FragmentPagerBinding
import com.boswelja.ephemeris.views.pager.InfiniteHorizontalPager

public class InfiniteHorizontalPagerFragment : Fragment() {
    public lateinit var pager: InfiniteHorizontalPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPagerBinding.inflate(inflater, container, false)
        pager = binding.pagerView
        return binding.root
    }
}
