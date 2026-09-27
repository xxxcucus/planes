package com.planes.android.screens.video

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun VideoYouTubeButton(entry: VideoModel, modifier: Modifier) {

    val context = LocalContext.current
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val borderColor = Color.Transparent

    Card(
        modifier.combinedClickable(
            onClick = { playLinkInYoutube(entry.getYoutubeLink(), context)}
        ).wrapContentHeight(),
        shape = RectangleShape,
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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