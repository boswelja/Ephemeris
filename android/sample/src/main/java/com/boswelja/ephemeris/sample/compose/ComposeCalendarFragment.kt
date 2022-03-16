package com.boswelja.ephemeris.sample.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.compose.EphemerisCalendar
import com.boswelja.ephemeris.compose.rememberCalendarState
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.CalendarWeekPageLoader
import com.boswelja.ephemeris.core.data.DisplayMonthFocusMode
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme
import kotlinx.datetime.DayOfWeek

class ComposeCalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                EphemerisTheme {
                    CalendarScreen()
                }
            }
        }
    }
}

@Composable
fun CalendarScreen() {
    val calendarState = rememberCalendarState(DisplayMonthFocusMode) {
        CalendarMonthPageLoader(
            DayOfWeek.SUNDAY
        )
    }
    Column {
        EphemerisCalendar(
            modifier = Modifier.semantics { contentDescription = "calendar" },
            calendarState = calendarState
        ) { dayState ->
            Text(
                modifier = Modifier.padding(16.dp).align(Alignment.Center),
                text = dayState.date.dayOfMonth.toString(),
                color = if (dayState.isFocusedDate)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                fontWeight = if (dayState.isFocusedDate) FontWeight.Bold else FontWeight.Normal
            )
        }
        Button(
            onClick = {
                if (calendarState.calendarPageLoader is CalendarMonthPageLoader) {
                    calendarState.calendarPageLoader = CalendarWeekPageLoader(
                        DayOfWeek.SUNDAY
                    )
                } else {
                    calendarState.calendarPageLoader = CalendarMonthPageLoader(
                        DayOfWeek.SUNDAY
                    )
                }
            }
        ) {
            Text("Switch")
        }
    }
}
