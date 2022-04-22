package com.boswelja.ephemeris.views.pager

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.views.CustomViewMatchers.withHeight
import com.boswelja.ephemeris.views.Direction
import com.boswelja.ephemeris.views.InfiniteAnimatingPagerFixedFragment
import com.boswelja.ephemeris.views.InfiniteAnimatingPagerFragment
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerActions.scrollTo
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerActions.smoothScrollTo
import com.boswelja.ephemeris.views.R
import com.boswelja.ephemeris.views.pagingadapters.AlternatingHeightPagerAdapter
import com.boswelja.ephemeris.views.pagingadapters.BasicInfinitePagerAdapter
import com.boswelja.ephemeris.views.pagingadapters.ChangeablePagingAdapter
import com.boswelja.ephemeris.views.testWithScrolling
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class InfiniteAnimatingPagerHeightTest {

    @Test
    fun ifFixedInitialSize_heightIsNotAnimated() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFixedFragment>()
        var pager: InfiniteAnimatingPager? = null
        scenario.onFragment {
            pager = it.pager
            it.pager.adapter = adapter
        }

        val initialHeight = pager!!.height
        testWithScrolling(
            onScroll = { _, direction ->
                val action = when (direction) {
                    Direction.LeftToRight -> swipeRight()
                    Direction.RightToLeft -> swipeLeft()
                }
                onView(withId(R.id.pager_view))
                    .perform(action)
                    .check(matches(withHeight(initialHeight)))
            }
        )
    }

    @Test
    fun initialViewHeight_isCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )

        val expectedHeight = adapter.getHeightFor(pager.currentPage)
        onView(withId(R.id.pager_view)).check(matches(withHeight(expectedHeight)))
    }

    @Test
    fun onUserScrolled_viewHeightIsCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )
        Thread.sleep(300)
        testWithScrolling(
            onScroll = { _, direction ->
                val action = when (direction) {
                    Direction.LeftToRight -> swipeRight()
                    Direction.RightToLeft -> swipeLeft()
                }
                val expectedHeight = adapter.getHeightFor(
                    when (direction) {
                        Direction.LeftToRight -> pager.currentPage - 1
                        Direction.RightToLeft -> pager.currentPage + 1
                    }
                )
                onView(withId(R.id.pager_view))
                    .perform(action)
                    .check(matches(withHeight(expectedHeight)))
                // Sleep a fraction to put an interval between swipes
                Thread.sleep(100)
            }
        )
    }

    @Test
    fun scrollToPosition_viewHeightIsCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )

        testWithScrolling(
            onScroll = { _, direction ->
                val nextPage = when (direction) {
                    Direction.LeftToRight -> pager.currentPage - 1
                    Direction.RightToLeft -> pager.currentPage + 1
                }
                onView(withId(R.id.pager_view))
                    .perform(scrollTo(nextPage))
                    .check(matches(withHeight(adapter.getHeightFor(nextPage))))
            }
        )
    }

    @Test
    fun smoothScrollToPosition_viewHeightIsCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(adapter = adapter)

        testWithScrolling(
            onScroll = { _, direction ->
                val nextPage = when (direction) {
                    Direction.LeftToRight -> pager.currentPage - 1
                    Direction.RightToLeft -> pager.currentPage + 1
                }
                onView(withId(R.id.pager_view))
                    .perform(smoothScrollTo(nextPage))
                    .check(matches(withHeight(adapter.getHeightFor(nextPage))))
            }
        )
    }

    @Test
    fun afterScroll_onItemChanged_heightChanges() {
        val initialHeight = 700
        val changedHeight = 500
        val adapter = ChangeablePagingAdapter(initialHeight, changedHeight)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )

        // Do a scroll
        onView(withId(R.id.pager_view)).perform(swipeLeft())

        repeat(4) {
            val useChangedHeight = it % 2 == 0
            scenario.onFragment {
                adapter.changeItem(pager.currentPage, useChangedHeight)
            }
            onView(withId(R.id.pager_view))
                .check(matches(withHeight(if (useChangedHeight) changedHeight else initialHeight)))
        }
    }

    @Test
    fun onItemChanged_heightChanges() {
        val initialHeight = 700
        val changedHeight = 500
        val adapter = ChangeablePagingAdapter(initialHeight, changedHeight)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )

        repeat(4) {
            val useChangedHeight = it % 2 == 0
            scenario.onFragment {
                adapter.changeItem(pager.currentPage, useChangedHeight)
            }
            onView(withId(R.id.pager_view))
                .check(matches(withHeight(if (useChangedHeight) changedHeight else initialHeight)))
        }
    }

    private fun FragmentScenario<InfiniteAnimatingPagerFragment>.initAndGetPager(
        adapter: InfinitePagerAdapter<*> = BasicInfinitePagerAdapter()
    ): InfiniteAnimatingPager {
        var pager: InfiniteAnimatingPager? = null
        onFragment {
            pager = it.pager
            it.pager.animateHeight = false
            it.pager.adapter = adapter
        }
        return pager!!
    }
}
