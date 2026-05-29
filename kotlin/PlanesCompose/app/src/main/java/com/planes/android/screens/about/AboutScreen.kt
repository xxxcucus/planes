package com.planes.android.screens.about

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun AboutScreen(modifier: Modifier, currentTitleState: MutableState<String>,
                currentScreenState: MutableState<String>,
                showPopupState: MutableState<Boolean>,
                context: Context, navController: NavController,
                aboutEntryList: List<AboutEntryModel>) {

    currentTitleState.value = stringResource(R.string.about)
    currentScreenState.value = PlanesScreens.Info.name
    showPopupState.value = false

    Surface(modifier = modifier,
        color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            LazyColumn {
                items(items = aboutEntryList) {
                    AboutEntryRow(entry = it, context)
                }
            }
        }
    }
}