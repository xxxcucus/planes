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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun AboutScreen(modifier: Modifier,
    navController: NavController,
                aboutEntryList: List<AboutEntryModel>) {

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