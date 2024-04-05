package com.boswelja.ephemeris.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boswelja.ephemeris.sample.collapsing.CollapsingCalendarScreen
import com.boswelja.ephemeris.sample.custompagesource.CustomSourceCalendarScreen
import com.boswelja.ephemeris.sample.dateselection.DateSelectionCalendarScreen
import com.boswelja.ephemeris.sample.ui.theme.EphemerisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EphemerisTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold {
        NavHost(
            navController = navController,
            startDestination = "root",
            modifier = Modifier.padding(it).fillMaxSize()
        ) {
            composable("root") {
                SampleSelectScreen(onNavigate = { navController.navigate(it) })
            }
            Sample.entries.forEach { sample ->
                composable(sample.route) {
                    sample.content()
                }
            }
        }
    }
}

enum class Sample(val sampleName: Int, val route: String, val content: @Composable () -> Unit) {
    COLLAPSING(
        sampleName = R.string.header_collapsing,
        route = "collapsing",
        content = { CollapsingCalendarScreen() }
    ),
    CUSTOM(
        sampleName = R.string.header_custom_source,
        route = "custom",
        content = { CustomSourceCalendarScreen() }
    ),
    SELECTION(
        sampleName = R.string.header_date_selection,
        route = "select",
        content = { DateSelectionCalendarScreen() }
    )
}
