package com.boswelja.ephemeris.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.ephemeris.compose.EphemerisMonthCalendar
import com.boswelja.ephemeris.compose.rememberCalendarState
import com.boswelja.ephemeris.core.model.toYearMonth
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme
import kotlinx.coroutines.flow.collect
import kotlinx.datetime.Clock

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
                    val state = rememberCalendarState(startMonth = Clock.System.now().toYearMonth())
                    LaunchedEffect(state) {
                        snapshotFlow { state.currentMonth }.collect {
                            Log.d("CurrentMonth", it.month.name)
                        }
                    }
                    EphemerisMonthCalendar(
                        calendarState = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) { dayState ->
                        Text(
                            text = dayState.date.dayOfMonth.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
