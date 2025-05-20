package com.planes.android.about

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.planes.android.R

@Composable
fun AboutEntryRow(entry: AboutEntryModel, context: Context) {

    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.padding(4.dp).
    fillMaxWidth())
    {
        Text(text = entry.getTitle(),
            style = MaterialTheme.typography.titleMedium)
        Text(text = entry.getText(),
            style = MaterialTheme.typography.bodyMedium)
        if (entry.hasButton()) {
            Button(modifier = Modifier.align(Alignment.End),
                onClick = {
                uriHandler.openUri(entry.getLinkButton())
            }) {
                Text(text = entry.getTextButton())
            }
        }
    }
}

