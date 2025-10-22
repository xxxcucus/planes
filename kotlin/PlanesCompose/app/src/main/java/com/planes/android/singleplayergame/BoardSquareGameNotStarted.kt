package com.planes.android.singleplayergame

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BoardSquareGameNotStarted(index: Int, squareSizeDp: Int, squareSizePx: Float,
                    planesGridViewModel: PlaneGridViewModel) {
    val row = index / planesGridViewModel.getColNo()
    val col = index % planesGridViewModel.getColNo()

    val pointOnPlane = planesGridViewModel.isPointOnPlane(col, row)

    val guess = planesGridViewModel.getGuessAtPosition(col, row)

    if (!pointOnPlane.first)
        GridSquareGameNotStarted(
            isComputer = planesGridViewModel.isComputer(),
            annotation = 0,
            guess = guess,
            widthDp = squareSizeDp,
            widthPx = squareSizePx,
            backgroundColor = Color.White,
            index = index
        )
    else {
        val annotation = planesGridViewModel.getAnnotation(pointOnPlane.second)
        val planesIdx = planesGridViewModel.decodeAnnotation(annotation)

        if (planesIdx.size == 1) {
            GridSquareGameNotStarted(
                isComputer = planesGridViewModel.isComputer(),
                annotation = if (planesIdx[0] < 0) -2 else planesIdx[0] + 1,
                guess = guess,
                widthDp = squareSizeDp,
                widthPx = squareSizePx,
                backgroundColor = Color.Blue,
                index = index
            )
            //Log.d("Planes", "plane ${planesIdx[0]}")
        } else {
            GridSquareGameNotStarted(
                isComputer = planesGridViewModel.isComputer(),
                annotation = -1,
                guess = guess,
                widthDp = squareSizeDp,
                widthPx = squareSizePx,
                backgroundColor = Color.Blue,
                index = index
            )
        }
    }
}