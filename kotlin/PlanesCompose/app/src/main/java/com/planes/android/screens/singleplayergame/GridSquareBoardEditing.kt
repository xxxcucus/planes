package com.planes.android.screens.singleplayergame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp


@Composable
fun GridSquareBoardEditing(widthDp: Dp, backgroundColor: Color) {
    Canvas(modifier = Modifier.width(widthDp).height(widthDp)
        .background(backgroundColor)) {
      drawRect(color = Color.Red, style = Stroke(width = 3f))
    }
}

@Composable
fun GridSquareBoardEditing(
    selectedPlane: Int,
    annotation: Int,
    widthDp: Dp,
    backgroundColor: Color,
    index: Int,
    onClick: (Int) -> Unit
) {
    Canvas(modifier = Modifier.width(widthDp).height(widthDp)
        .background(backgroundColor)
            .clickable {
                onClick.invoke(index)
            })
     {

         val planeOverlapColor = Color.Red
        var squareColor = backgroundColor
        val cockpitColor = Color.Blue
        val selectedPlaneColor = Color.Black
        val firstPlaneColor = Color(80, 80, 80)
        val secondPlaneColor = Color(120, 120, 120)
        val thirdPlaneColor = Color(160, 160, 160)

        //Log.d("Planes", "Annotation $annotation")
        //if (isComputer) {
         when (annotation) {
             -1 -> {
                 squareColor = planeOverlapColor
             }
             -2 -> {
                 squareColor = cockpitColor
             }
             selectedPlane + 1 -> {
                 squareColor = selectedPlaneColor
             }
             1 -> {
                 squareColor = firstPlaneColor
             }
             2 -> {
                 squareColor = secondPlaneColor
             }
             3 -> {
                 squareColor = thirdPlaneColor
             }
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




