package com.planes.android.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planes.android.R

@Composable
fun DrawerMenuItemGeneric(name: String, iconId: Int, enabled: Boolean, onClickAction: () -> Unit) {
    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = name,
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
            if (enabled) {
                onClickAction()
                Log.d("Planes", "Jump to $name")
            }
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Color.Transparent,
            unselectedContainerColor = Color.Transparent,
            selectedIconColor = Color.Transparent,
            unselectedIconColor = if (enabled) Color.Black else Color.Gray,
            selectedTextColor = Color.Transparent,
            unselectedTextColor = if (enabled) Color.Black else Color.Gray
        )
    )
}