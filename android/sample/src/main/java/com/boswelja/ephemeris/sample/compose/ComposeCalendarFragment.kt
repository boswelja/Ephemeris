package com.boswelja.ephemeris.sample.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.boswelja.ephemeris.compose.EphemerisCalendar
import com.boswelja.ephemeris.compose.rememberCalendarState
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.core.data.CalendarWeekPageSource
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
fun CalendarScreen(
    headerDateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
) {
    val calendarState = rememberCalendarState {
        CalendarMonthPageSource(
            DayOfWeek.SUNDAY
        )
    }

    var headerText by remember { mutableStateOf("") }

    LaunchedEffect(calendarState) {
        calendarState.displayedDateRange.collectLatest {
            headerText = "${it.start.toJavaLocalDate().format(headerDateFormatter)} - ${it.endInclusive.toJavaLocalDate().format(headerDateFormatter)}"
        }
    }

    Column {
        Text(
            text = headerText,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
        )
        EphemerisCalendar(
            contentPadding = PaddingValues(horizontal = 16.dp),
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
                if (calendarState.pageSource is CalendarMonthPageSource) {
                    calendarState.pageSource = CalendarWeekPageSource(
                        DayOfWeek.SUNDAY
                    )
                } else {
                    calendarState.pageSource = CalendarMonthPageSource(
                        DayOfWeek.SUNDAY
                    )
                }
            }
        ) {
            Text("Switch")
        }
    }
}
