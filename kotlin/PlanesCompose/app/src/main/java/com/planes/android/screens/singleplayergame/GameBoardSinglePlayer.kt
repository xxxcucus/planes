package com.planes.android.screens.singleplayergame

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy

@Composable
fun GameBoardSinglePlayer(rows: Int, cols: Int, modifier: Modifier = Modifier,
                          content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = gameBoardSinglePlayerMeasurePolicy(rows, cols)
    )
}

fun gameBoardSinglePlayerMeasurePolicy(rows: Int, cols: Int): MeasurePolicy =
    MeasurePolicy { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(
                maxWidth = constraints.maxWidth / cols,
                maxHeight = constraints.maxHeight / rows,
                minWidth = constraints.maxWidth / cols,
                minHeight = constraints.maxHeight / rows))
        }


        layout(constraints.maxWidth, constraints.maxHeight) {
            var yPos = 0
            var xPos = 0
            var maxY = 0

            placeables.forEachIndexed { index, placeable ->
                //if (xPos + placeable.width >= constraints.maxWidth) {
                if (index % cols == 0) {
                    xPos = 0
                    yPos += maxY
                    maxY = 0
                }

                placeable.placeRelative(
                    x = xPos,
                    y = yPos
                )

                xPos += placeable.width

                if (maxY < placeable.height) {
                    maxY = placeable.height
                }
            }
        }
    }

