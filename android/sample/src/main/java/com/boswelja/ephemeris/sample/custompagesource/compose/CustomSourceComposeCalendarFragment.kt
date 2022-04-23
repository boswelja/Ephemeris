package com.boswelja.ephemeris.sample.custompagesource.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.compose.EphemerisCalendar
import com.boswelja.ephemeris.compose.rememberCalendarState
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.custompagesource.CustomCalendarPageSource
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme

class CustomSourceComposeCalendarFragment : Fragment() {
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
fun CalendarScreen(
    modifier: Modifier = Modifier
) {
    val calendarState = rememberCalendarState { CustomCalendarPageSource() }

    Column(modifier) {
        EphemerisCalendar(
            contentPadding = PaddingValues(horizontal = 16.dp),
            calendarState = calendarState
        ) { calendarDay ->
            CollapsingCalendarDate(
                calendarDay = calendarDay,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
internal fun CollapsingCalendarDate(
    calendarDay: CalendarDay,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = calendarDay.date.dayOfMonth.toString(),
        color = if (calendarDay.isFocusedDate)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface,
        fontWeight = if (calendarDay.isFocusedDate) FontWeight.Bold else FontWeight.Normal
    )
}
