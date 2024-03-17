package com.boswelja.ephemeris.sample.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.databinding.FragmentMainBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        val adapter = SampleAdapter {
            navController.navigate(it)
        }
        adapter.submitList(createItemsList())
        binding.root.adapter = adapter
    }

    private fun createItemsList(): List<MainItem> =
        listOf(
            Header(getString(R.string.header_collapsing)),
            Sample(
                navAction = R.id.action_mainFragment_to_collapsingComposeCalendarFragment,
                text = getString(R.string.compose_sample)
            ),
            Header(getString(R.string.header_custom_source)),
            Sample(
                navAction = R.id.action_mainFragment_to_customSourceComposeCalendarFragment,
                text = getString(R.string.compose_sample)
            ),
            Header(getString(R.string.header_date_selection)),
            Sample(
                navAction = R.id.action_mainFragment_to_dateSelectionComposeCalendarFragment,
                text = getString(R.string.compose_sample)
            ),
        )
}
