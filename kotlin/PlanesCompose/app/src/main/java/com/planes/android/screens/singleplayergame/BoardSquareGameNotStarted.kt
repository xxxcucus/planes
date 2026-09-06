package com.planes.android.screens.singleplayergame

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
fun BoardSquareGameNotStarted(
    index: Int, squareSizeDp: Dp, squareSizePx: Float,
    planesGridViewModel: PlaneGridViewModel
) {
    val row = index / planesGridViewModel.getColNo()
    val col = index % planesGridViewModel.getColNo()

    val pointOnPlane = planesGridViewModel.isPointOnPlane(col, row)

    val guess = planesGridViewModel.getGuessAtPosition(col, row)

    if (!pointOnPlane.first)
        GridSquareGameNotStarted(
            annotation = 0,
            guess = guess,
            widthDp = squareSizeDp,
            backgroundColor = MaterialTheme.colorScheme.background
        )
    else {
        val annotation = planesGridViewModel.getAnnotation(pointOnPlane.second)
        val planesIdx = planesGridViewModel.decodeAnnotation(annotation)

        if (planesIdx.size == 1) {
            GridSquareGameNotStarted(
                annotation = if (planesIdx[0] < 0) -2 else planesIdx[0] + 1,
                guess = guess,
                widthDp = squareSizeDp,
                backgroundColor = MaterialTheme.colorScheme.background
            )
            //Log.d("Planes", "plane ${planesIdx[0]}")
        } else {
            GridSquareGameNotStarted(
                annotation = -1,
                guess = guess,
                widthDp = squareSizeDp,
                backgroundColor = MaterialTheme.colorScheme.background
            )
        }
    }
}