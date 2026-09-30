package com.planes.android.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class YouTubeUtils {
    companion object{
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
    }
}