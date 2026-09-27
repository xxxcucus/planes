package com.planes.android.screens.video

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.planes.android.screens.about.AboutEntryModel

@Composable
fun VideoEntryRow(entry: VideoModel) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(4.dp).fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    )
    {
        Text(
            text = entry.getVideoName(),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = entry.getVideoDescription(),
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                playLinkInYoutube(entry.getYoutubeLink(), context)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = "Play Video") //TODO: translations
        }

    }
}

/*fun playLinkInYoutube(link: String, context: Context) {
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
}*/
