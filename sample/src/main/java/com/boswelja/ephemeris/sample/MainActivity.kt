package com.boswelja.ephemeris.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.ephemeris.compose.EphemerisCalendar
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme

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
                    EphemerisCalendar(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) { dayState ->
                        Text(
                            text = dayState.date.dayOfMonth.toString(),
                            modifier = Modifier.weight(1f).aspectRatio(1f).padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
