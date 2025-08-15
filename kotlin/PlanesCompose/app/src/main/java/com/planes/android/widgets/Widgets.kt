package com.planes.android.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxOption(text: String,
                   selected: Boolean,
                   onClickAction: () -> Unit) {
    Text(text = text,
        modifier = Modifier.padding(start = 15.dp, end = 15.dp).
        fillMaxWidth().
        border(width = 4.dp, color = if (selected)  Color.Blue else Color.Transparent,
            shape = RoundedCornerShape(15.dp)
        ).clip(
            RoundedCornerShape(topStartPercent = 50, topEndPercent = 50,
            bottomEndPercent = 50, bottomStartPercent = 50)
        ).clickable {
            onClickAction.invoke()
        },
        textAlign = TextAlign.Center)
}