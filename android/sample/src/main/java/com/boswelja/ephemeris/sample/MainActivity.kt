package com.boswelja.ephemeris.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.boswelja.ephemeris.compose.EphemerisCalendar
import com.boswelja.ephemeris.compose.rememberCalendarState
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme
import kotlinx.coroutines.flow.collect
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EphemerisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val state = rememberCalendarState(
                            startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                            firstDayOfWeek = DayOfWeek.SUNDAY,
                            initialPageSize = PageSize.MONTH
                        )
                        LaunchedEffect(state) {
                            snapshotFlow { state.currentMonth }.collect {
                                Log.d("CurrentMonth", it.month.name)
                            }
                        }
                        EphemerisCalendar(
                            calendarState = state,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) { dayState ->
                            Text(
                                text = dayState.date.dayOfMonth.toString(),
                                color = if (dayState.isFocusedDate) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (dayState.isFocusedDate) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                            )
                        }
                        Text(text = state.currentMonth.month.name)
                    }
                }
            }
        }
    }
}
