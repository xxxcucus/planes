package com.planes.android.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.planes.android.navigation.PlanesScreens
import kotlinx.coroutines.CoroutineScope

@Composable
fun AboutScreen(modifier: Modifier, currentScreenState: MutableState<String>,
    navController: NavController,
                aboutEntryList: List<AboutEntryModel>) {

    currentScreenState.value = PlanesScreens.Info.name

    Surface(modifier = modifier,
        color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            LazyColumn {
                items(items = aboutEntryList) {
                    AboutEntryRow(entry = it)
                }
            }
        }
    }
}