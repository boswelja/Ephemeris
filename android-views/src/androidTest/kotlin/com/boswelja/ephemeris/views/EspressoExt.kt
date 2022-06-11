package com.boswelja.ephemeris.views

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
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

    fun animateScrollTo(date: LocalDate): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(EphemerisCalendarView::class.java)
                )
            }

            override fun getDescription(): String {
                return "performing smooth scroll to date $date"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as EphemerisCalendarView).scrollToDate(date)
                uiController?.loopMainThreadForAtLeast(300)
            }
        }
    }
}

object CustomViewMatchers {
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

    fun withPadding(left: Int, top: Int, right: Int, bottom: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.apply {
                    appendText("Checking the matcher on received view: ")
                    appendText("with paddingLeft=$left paddingTop=$top paddingRight=$right paddingBottom=$bottom")
                }
            }

            override fun matchesSafely(item: View?): Boolean {
                return item?.let {
                    it.paddingLeft == left && it.paddingTop == top && it.paddingRight == right && it.paddingBottom == bottom
                } ?: false
            }
        }
    }

    fun withClipToPadding(clipToPadding: Boolean): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.apply {
                    appendText("Checking the matcher on received view: ")
                    appendText("with clipToPadding=$clipToPadding")
                }
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item !is ViewGroup) return false
                return item.clipToPadding == clipToPadding
            }
        }
    }
}
