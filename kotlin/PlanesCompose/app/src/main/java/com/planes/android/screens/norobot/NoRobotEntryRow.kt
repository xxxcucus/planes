package com.planes.android.screens.norobot

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NoRobotEntryRow(noRobotViewModel: NoRobotViewModel, index: Int,
                    sizeDp: Int, horizontal: Boolean) {

    val entry = noRobotViewModel.getImage(index)
    val selected = noRobotViewModel.getSelected(index)

    var borderColor = Color.Green
    if (selected)
        borderColor = Color.Red

    Card(
        modifier = Modifier.size(sizeDp.dp)
            .clickable {
                Log.d("PlanesCompose", "Toggle selected $index $selected")
                noRobotViewModel.toggleSelected(index)
            },
        shape = RectangleShape,
        border = BorderStroke(2.dp, borderColor)
    ) {
        var cs = ContentScale.FillWidth

        if (horizontal)
            cs = ContentScale.FillHeight

        Image(
            painterResource(entry.m_ImageId),
            contentDescription = "",
            contentScale = cs,
            modifier = Modifier.fillMaxSize()
        )
    }
}