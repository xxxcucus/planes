package com.planes.android.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutEntryRow(entry: AboutEntryModel) {

    Column(modifier = Modifier.padding(4.dp).
    fillMaxWidth())
    {
        Text(text = entry.getTitle(),
            style = MaterialTheme.typography.titleMedium)
        Text(text = entry.getText(),
            style = MaterialTheme.typography.bodyMedium)

    }
}