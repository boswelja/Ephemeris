package com.boswelja.ephemeris.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.views.databinding.FragmentCalendarBinding

public class EphemerisCalendarFragment : Fragment() {
    public lateinit var calendarView: EphemerisCalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarView = binding.calendarView
        return binding.root
    }
}
