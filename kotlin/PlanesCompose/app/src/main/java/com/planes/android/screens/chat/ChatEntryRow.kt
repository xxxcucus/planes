package com.planes.android.screens.chat

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens
import com.planes.android.utils.DateTimeUtils
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun ChatEntryRow(user: UserWithLastLoginResponse,
                 navController: NavController) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = Modifier.padding(4.dp).fillMaxWidth()
        .clickable {
            navController.navigate(route = PlanesScreens.Conversation.name)
        })
    {
        Text(text = user.m_UserName,
            style = MaterialTheme.typography.titleMedium)

        Text(modifier = Modifier.align(Alignment.End),
            style = MaterialTheme.typography.titleMedium,
            text = if (isPlayerOnline(user)) "Online" else "Offline")
    }
}

fun isPlayerOnline(user: UserWithLastLoginResponse): Boolean {
    val date = DateTimeUtils.parseDate(user.m_LastLogin)
    if (date == null)
        return false

    val diff = getDateDiff(date, Date.from(Instant.now()), TimeUnit.MINUTES)

    if (user.m_UserName.startsWith("test")) {
        Log.d("PLanes", "User ${user.m_UserName} last login ${user.m_LastLogin} diff $diff")
    }

    return diff < 30L;
}
fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillis: Long = date2.time - date1.time
    return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS)
}