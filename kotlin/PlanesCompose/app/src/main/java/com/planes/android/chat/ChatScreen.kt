package com.planes.android.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens

@Composable
fun ChatScreen(modifier: Modifier,
               currentScreenState: MutableState<String>, navController: NavController) {

    currentScreenState.value = PlanesScreens.Chat.name

    Column(modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Chat Screen")
    }
}