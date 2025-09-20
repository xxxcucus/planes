package com.planes.android.singleplayergame

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BoardSquare(index: Int, squareSizeDp: Int, squareSizePx: Float,
                planesGridViewModel: PlaneGridViewModel, isHoriz: Boolean,
                onClick: (Int) -> Unit) {
    val row = index / planesGridViewModel.getColNo()
    val col = index % planesGridViewModel.getColNo()

    var pointOnPlane = planesGridViewModel.isPointOnPlane(col, row)
    if (isHoriz)
        pointOnPlane = planesGridViewModel.isPointOnPlane(row, col)
    if (!pointOnPlane.first)
        GridSquare(squareSizeDp, squareSizePx, Color.Blue)
    else {
        val annotation = planesGridViewModel.getAnnotation(pointOnPlane.second)
        val planesIdx = planesGridViewModel.decodeAnnotation(annotation)
        if (planesIdx.size == 1) {
            GridSquare(
                isComputer = planesGridViewModel.isComputer(),
                selectedPlane = planesGridViewModel.getSelectedPlane(),
                annotation = if (planesIdx[0] < 0) -2 else planesIdx[0] + 1,
                widthDp = squareSizeDp,
                widthPx = squareSizePx,
                backgroundColor = Color.Blue,
                index = index,
                onClick = onClick
            )
            //Log.d("Planes", "plane ${planesIdx[0]}")
        } else {
            GridSquare(
                isComputer = planesGridViewModel.isComputer(),
                selectedPlane = planesGridViewModel.getSelectedPlane(),
                annotation = -1,
                widthDp = squareSizeDp,
                widthPx = squareSizePx,
                backgroundColor = Color.Blue,
                index = index,
                onClick = onClick
            )
        }
    }
}