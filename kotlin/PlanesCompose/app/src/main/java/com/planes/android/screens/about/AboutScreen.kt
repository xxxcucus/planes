package com.planes.android.screens.about

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.planes.android.R
import com.planes.android.navigation.PlanesScreens

@Composable
fun AboutScreen(modifier: Modifier, currentTitleState: MutableState<String>,
                currentScreenState: MutableState<String>,
                showPopupState: MutableState<Boolean>,
                aboutEntryList: List<AboutEntryModel>) {

    currentTitleState.value = stringResource(R.string.about)
    currentScreenState.value = PlanesScreens.Info.name
    showPopupState.value = false

    Log.d("Planes", "Background = ${MaterialTheme.colorScheme.background}")


    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(items = aboutEntryList) {
                    AboutEntryRow(entry = it)
                }
            }
        }
    }

}