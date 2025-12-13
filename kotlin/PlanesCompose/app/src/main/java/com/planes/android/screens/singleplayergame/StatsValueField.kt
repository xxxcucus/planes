package com.planes.android.screens.singleplayergame

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun StatsValueField(value: Int,
                    enabled: Boolean,
                    modifier: Modifier,
                    hot: Boolean
) {
    val color = if (enabled) MaterialTheme.colorScheme.surfaceVariant
    else MaterialTheme.colorScheme.secondary

    Card(
        modifier = modifier.padding(1.dp),
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
                text = "$value", //TODO: compute text size dynamically
                color = if (hot) Color.Red else Color.Black
            )
        }
    }
}