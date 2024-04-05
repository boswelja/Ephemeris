package com.boswelja.ephemeris.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun SampleSelectScreen(
    onNavigate: (route: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(Sample.entries) {
            ListItem(
                headlineContent = {
                    Text(stringResource(it.sampleName))
                },
                modifier = Modifier.clickable { onNavigate(it.route) }
            )
        }
    }
}
