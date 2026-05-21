package com.planes.android.screens.chat

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens
import com.planes.android.utils.DateTimeUtils
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun ChatEntryRow(user: UserWithLastLoginAndNewMessagesFlag,
                 userId: Long, username: String,
                 navController: NavController,
                 chatUserListViewModel: ChatUserListViewModel) {
    val uriHandler = LocalUriHandler.current

    Row(modifier = Modifier.padding(4.dp).fillMaxWidth()
        .clickable {
            chatUserListViewModel.setMessagesUnread(user.m_UserName, user.m_UserId.toLong(),
                username, userId)
            navController.navigate(route = "${PlanesScreens.Conversation.name}/${user.m_UserId}/${user.m_UserName}")
        })
    {
        Text(text = user.m_UserName,
            textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium)

        if (user.m_NewMessagesFlag) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "New Messages Icon",
                modifier = Modifier.weight(1f).
                wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Text(textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            text = if (isPlayerOnline(user)) "Online" else "Offline")
    }
}

fun isPlayerOnline(user: UserWithLastLoginAndNewMessagesFlag): Boolean {
    val date = DateTimeUtils.parseDate(user.m_LastLogin) ?: return false

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