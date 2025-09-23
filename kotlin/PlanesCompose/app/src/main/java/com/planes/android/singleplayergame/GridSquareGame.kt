package com.planes.android.singleplayergame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.planes.singleplayerengine.GuessPoint

@Composable
fun GridSquareGame(isComputer: Boolean,
                   annotation: Int,
                   guess: GuessPoint?,
                   widthDp: Int, widthPx: Float,
                   backgroundColor: Color,
                   index: Int,
                   onClick: (Int) -> Unit) {
    Canvas(modifier = Modifier.width(widthDp.dp).height(widthDp.dp).
    clickable {
        onClick.invoke(index)
    }) {

        val planeOverlapColor = Color.Red
        var squareColor = backgroundColor
        val cockpitColor = Color.Blue
        val firstPlaneColor = Color(80, 80, 80)
        val secondPlaneColor = Color(120, 120, 120)
        val thirdPlaneColor = Color(160, 160, 160)

        //Log.d("Planes", "Annotation $annotation")
        //if (isComputer) {
        if (annotation == -1) {
            squareColor = planeOverlapColor
        } else if (annotation == -2) {
            squareColor = cockpitColor
        }  else if (annotation == 1) {
            squareColor = firstPlaneColor
        } else if (annotation == 2) {
            squareColor = secondPlaneColor
        } else if (annotation == 3) {
            squareColor = thirdPlaneColor
        }

        //}

        drawRect(brush = Brush.linearGradient(
            colors = listOf(squareColor, Color(0xFF81C784)), // dark to light green
            start = Offset.Zero,
            end = Offset(size.width, size.height)
        ), style = Fill
        )
    }
}