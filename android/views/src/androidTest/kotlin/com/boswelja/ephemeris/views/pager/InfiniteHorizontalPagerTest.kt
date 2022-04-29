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
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerActions.scrollTo
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerActions.smoothScrollTo
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerActions.withCurrentPage
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerFragment
import com.boswelja.ephemeris.views.R
import com.boswelja.ephemeris.views.pagingadapters.AlternatingHeightPagerAdapter
import com.boswelja.ephemeris.views.pagingadapters.BasicInfinitePagerAdapter
import com.boswelja.ephemeris.views.testWithScrolling
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class InfiniteHorizontalPagerTest {

    @Test
    fun userScrolling_updatesCurrentPage() {
        // Init pager
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        testWithScrolling(
            onScroll = { _, direction ->
                val currentPage = pager.currentPage
                val action = when (direction) {
                    Direction.LeftToRight -> swipeRight()
                    Direction.RightToLeft -> swipeLeft()
                }
                val pageMatcher = when (direction) {
                    Direction.LeftToRight -> withCurrentPage(currentPage - 1)
                    Direction.RightToLeft -> withCurrentPage(currentPage + 1)
                }
                onView(withId(R.id.pager_view))
                    .perform(action)
                    .check(matches(pageMatcher))
            },
            onResetState = {
                scenario.onFragment {
                    it.pager.scrollToPosition(0)
                }
            }
        )
    }

    @Test
    fun smoothScrollToPosition_updatesCurrentPage() {
        // Init pager
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        testWithScrolling(
            onScroll = { _, direction ->
                val nextPage = when (direction) {
                    Direction.LeftToRight -> pager.currentPage - 1
                    Direction.RightToLeft -> pager.currentPage + 1
                }
                onView(withId(R.id.pager_view))
                    .perform(smoothScrollTo(nextPage))
                    .check(matches(withCurrentPage(nextPage)))
            },
            onResetState = {
                scenario.onFragment {
                    it.pager.scrollToPosition(0)
                }
            }
        )
    }

    @Test
    fun scrollToPosition_updatesCurrentPage() {
        // Init pager
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        testWithScrolling(
            onScroll = { _, direction ->
                val nextPage = when (direction) {
                    Direction.LeftToRight -> pager.currentPage - 1
                    Direction.RightToLeft -> pager.currentPage + 1
                }
                onView(withId(R.id.pager_view))
                    .perform(scrollTo(nextPage))
                    .check(matches(withCurrentPage(nextPage)))
            },
            onResetState = {
                scenario.onFragment {
                    it.pager.scrollToPosition(0)
                }
            }
        )
    }

    @Test
    fun canScrollFar() {
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        scenario.initAndGetPager()

        val targetForwardPage = 100000
        onView(withId(R.id.pager_view))
            .perform(scrollTo(targetForwardPage))
            .check(matches(withCurrentPage(targetForwardPage)))

        val targetBackwardPage = -100000
        onView(withId(R.id.pager_view))
            .perform(scrollTo(targetBackwardPage))
            .check(matches(withCurrentPage(targetBackwardPage)))
    }

    @Test
    fun userScrolls_heightIsCorrect() {
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val adapter = AlternatingHeightPagerAdapter(300, 500)
        val pager = scenario.initAndGetPager(adapter)

        testWithScrolling(
            onScroll = { _, direction ->
                val action = when (direction) {
                    Direction.LeftToRight -> swipeRight()
                    Direction.RightToLeft -> swipeLeft()
                }
                onView(withId(R.id.pager_view))
                    .perform(action)
                val newPage = pager.currentPage
                val newHeight = adapter.getHeightFor(newPage)
                onView(withId(R.id.pager_view))
                    .check(matches(withHeight(newHeight)))
            },
            onResetState = {
                scenario.onFragment {
                    it.pager.scrollToPosition(0)
                }
            }
        )
    }

    private fun FragmentScenario<InfiniteHorizontalPagerFragment>.initAndGetPager(
        adapter: InfinitePagerAdapter<*> = BasicInfinitePagerAdapter()
    ): InfiniteHorizontalPager {
        var pager: InfiniteHorizontalPager? = null
        onFragment {
            pager = it.pager
            it.pager.adapter = adapter
            // TODO For some reason the initial page isn't set correctly in tests
            it.pager.scrollToPosition(0)
        }
        return pager!!
    }
}
