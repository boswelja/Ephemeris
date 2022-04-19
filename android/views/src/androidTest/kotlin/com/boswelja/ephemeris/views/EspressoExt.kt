package com.boswelja.ephemeris.views

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.boswelja.ephemeris.views.pager.InfiniteHorizontalPager
import kotlinx.datetime.LocalDate
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

object EphemerisCalendarViewActions {
    fun scrollTo(date: LocalDate): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(EphemerisCalendarView::class.java)
                )
            }

            override fun getDescription(): String {
                return "performing scroll to date $date"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as EphemerisCalendarView).scrollToDate(date)
                uiController?.loopMainThreadUntilIdle()
            }
        }
    }
}

object InfiniteHorizontalPagerActions {
    fun withCurrentPage(currentPage: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.apply {
                    appendText("Checking the matcher on received view: ")
                    appendText("with currentPage=$currentPage")
                }
            }

            override fun matchesSafely(item: View?): Boolean {
                require(item is InfiniteHorizontalPager?)
                return item?.currentPage == currentPage
            }
        }
    }

    fun smoothScrollTo(page: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "performing smooth scroll to page $page"
            }

            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(InfiniteHorizontalPager::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                require(view is InfiniteHorizontalPager)
                view.smoothScrollToPosition(page)
                uiController?.loopMainThreadForAtLeast(300) // 300ms to account for scrolling
            }
        }
    }

    fun scrollTo(page: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "performing scroll to page $page"
            }

            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(InfiniteHorizontalPager::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                require(view is InfiniteHorizontalPager)
                view.scrollToPosition(page)
                uiController?.loopMainThreadUntilIdle()
            }
        }
    }
}

object CustomViewMatchers {
    fun withHeight(height: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                return item?.height == height
            }

            override fun describeTo(description: Description?) {
                description?.apply {
                    appendText("Checking the matcher on received view: ")
                    appendText("with height=$height")
                }
            }
        }
    }

    fun withBackgroundColor(color: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.apply {
                    appendText("Checking the matcher on received view: ")
                    appendText("with backgroundColor=$color")
                }
            }

            override fun matchesSafely(item: View?): Boolean {
                return (item?.background is ColorDrawable) && (item.background as ColorDrawable).color == color
            }
        }
    }

    fun hasChild(matcher: Matcher<View>): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.apply {
                    appendText("Checking the matcher on received view: ")
                    appendText("with any child matching=$matcher")
                }
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item !is ViewGroup) return false
                return item.children.any { matcher.matches(it) }
            }
        }
    }
}
