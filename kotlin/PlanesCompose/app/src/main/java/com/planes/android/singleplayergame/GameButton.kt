package com.planes.android.singleplayergame

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun GameButton(title: String, planesGridViewModel: PlaneGridViewModel,
               modifier: Modifier,
               enabled: Boolean,
               onClick: (PlaneGridViewModel) -> Unit
) {
    val color = if (enabled) MaterialTheme.colorScheme.surfaceVariant
    else MaterialTheme.colorScheme.secondary

    Card(
        modifier = modifier.padding(1.dp).clickable {
            if (enabled)
                onClick.invoke(planesGridViewModel)
        },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = title //TODO: compute text size dynamically
            )
        }
    }
}