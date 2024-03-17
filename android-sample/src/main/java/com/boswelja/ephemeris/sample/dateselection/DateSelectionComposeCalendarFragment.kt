package com.boswelja.ephemeris.sample.dateselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boswelja.ephemeris.core.ui.EphemerisCalendar
import com.boswelja.ephemeris.core.ui.rememberCalendarState
import com.boswelja.ephemeris.core.data.CalendarMonthPageSource
import com.boswelja.ephemeris.sample.R
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateSelectionComposeCalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                EphemerisTheme {
                    DateSelectionCalendarScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun DateSelectionCalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: DateSelectionViewModel = viewModel()
) {
    val calendarState = rememberCalendarState()

    val selectedDate by viewModel.selectedDate.collectAsState()

    Column(modifier) {
        Text(
            text = stringResource(
                R.string.selected_date,
                selectedDate.toJavaLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            ),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
        )
        EphemerisCalendar(
            pageSource = remember { CalendarMonthPageSource(firstDayOfWeek = DayOfWeek.SUNDAY) },
            calendarState = calendarState
        ) { dateState ->
            DateSelectionCalendarCell(
                date = dateState.date,
                isSelected = dateState.date == selectedDate,
                isFocused = dateState.isFocusedDate,
                onClick = { viewModel.selectDate(dateState.date) },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .padding(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DateSelectionCalendarCell(
    date: LocalDate,
    isSelected: Boolean,
    isFocused: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surfaceColor by animateColorAsState(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
    Surface(
        onClick = onClick,
        enabled = isFocused,
        color = surfaceColor,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (isFocused)
                    LocalContentColor.current
                else
                    LocalContentColor.current.copy(alpha = 0.4f),
                fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
