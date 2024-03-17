package com.boswelja.ephemeris.sample.custompagesource

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.boswelja.ephemeris.core.ui.EphemerisCalendar
import com.boswelja.ephemeris.core.ui.rememberCalendarState
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.sample.R

@Composable
fun CustomSourceCalendarScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    val calendarState = rememberCalendarState()
    val pageSource = remember { CustomCalendarPageSource() }

    Column(modifier) {
        Row(Modifier.padding(contentPadding)) {
            Text(
                text = stringResource(R.string.day_mon),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.day_tue),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.day_wed),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.day_thu),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.day_fri),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        EphemerisCalendar(
            pageSource = pageSource,
            contentPadding = contentPadding,
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
        color = if (calendarDay.isFocusedDate) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        fontWeight = if (calendarDay.isFocusedDate) FontWeight.Bold else FontWeight.Normal
    )
}
