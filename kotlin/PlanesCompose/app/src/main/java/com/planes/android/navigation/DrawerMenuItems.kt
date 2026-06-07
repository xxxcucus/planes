package com.planes.android.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planes.android.R

@Composable
fun DrawerMenuItemGeneric(name: String, iconId: Int, onClickAction: () -> Unit) {
    NavigationDrawerItem(
        icon = {
            /*Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Navigation Icon",
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    .size(28.dp)
            )*/

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Star",
                Modifier
                    .size(50.dp)
                    .padding(start = 16.dp, end = 8.dp)
            )
        },
        label = {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        selected = false,
        onClick = {
            onClickAction()
            Log.d("Planes", "Jump to $name")
        }
    )
}