package com.planes.android.singleplayergame

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy

@Composable
fun GameBoardSinglePlayer(modifier: Modifier = Modifier,
                          content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = gameBoardSinglePlayerMeasurePolicy()
    )
}

fun gameBoardSinglePlayerMeasurePolicy(): MeasurePolicy =
    MeasurePolicy { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(
                maxWidth = constraints.maxWidth / 10,
                maxHeight = constraints.maxHeight / 10,
                minWidth = constraints.maxWidth / 10,
                minHeight = constraints.maxHeight / 10))
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var yPos = 0
            var xPos = 0
            var maxY = 0

            placeables.forEach { placeable ->
                if (xPos + placeable.width >= constraints.maxWidth) {
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

