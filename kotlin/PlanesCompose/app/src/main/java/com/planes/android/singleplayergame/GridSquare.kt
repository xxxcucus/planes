package com.planes.android.singleplayergame

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun GridSquare(widthDp: Int, widthPx: Float, backgroundColor: Color) {
    Canvas(modifier = Modifier.width(widthDp.dp).height(widthDp.dp)) {
      drawRect(color = Color.Red, style = Stroke(width = 3f))
    }
}

@Composable
fun GridSquare(isComputer: Boolean, annotation: Int, widthDp: Int, widthPx: Float, backgroundColor: Color) {
    Canvas(modifier = Modifier.width(widthDp.dp).height(widthDp.dp)) {
        val selected = 0

        val planeOverlapColor = Color.Red
        var squareColor = planeOverlapColor
        val cockpitColor = Color.Blue
        val selectedPlaneColor = Color.Black
        val firstPlaneColor = Color(40, 40, 40)
        val secondPlaneColor = Color(80, 80, 80)
        val thirdPlaneColor = Color(120, 120, 120)

        Log.d("Planes", "Annotation $annotation")
        //if (isComputer) {
            if (annotation == -1) {
                squareColor = planeOverlapColor
            } else if (annotation == -2) {
                squareColor = cockpitColor
            } else if (annotation == 0) {
                squareColor = selectedPlaneColor
            } /*else if (annotation - 1 == 0 ) {
                squareColor = selectedPlaneColor
            }*/ else if (annotation == 1) {
                squareColor = firstPlaneColor
            } else if (annotation == 2) {
                squareColor = secondPlaneColor
            } else if (annotation == 3) {
                squareColor = thirdPlaneColor
            }

        //}

        drawRect(color = squareColor, style = Fill)
    }
}