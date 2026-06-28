package com.planes.android.screens.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri

@Composable
fun VideoButton(entry: VideoModel, currentVideoState: MutableState<Int>, modifier: Modifier) {

    val color = if (currentVideoState.value != entry.getVideoId())
        MaterialTheme.colorScheme.surfaceVariant
    else MaterialTheme.colorScheme.secondary

    val context = LocalContext.current

    val isSelected = currentVideoState.value == entry.getVideoId()

    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.surfaceVariant

    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.tertiary
        else Color.Transparent

    Card(
        modifier.combinedClickable(
            onClick = { currentVideoState.value = entry.getVideoId() },
            onLongClick = { playLinkInYoutube(entry.getYoutubeLink(), context)}
        ).wrapContentHeight(),
        shape = RectangleShape,
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.getVideoName(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun playLinkInYoutube(link: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, link.toUri()).apply {
        // Force the link to open directly in the YouTube app
        setPackage("com.google.android.youtube")
    }

    // Fallback to a web browser if the YouTube app is uninstalled
    if (context.packageManager.resolveActivity(intent, 0) != null) {
        context.startActivity(intent)
    } else {
        val browserIntent = Intent(Intent.ACTION_VIEW, link.toUri())
        context.startActivity(browserIntent)
    }
}