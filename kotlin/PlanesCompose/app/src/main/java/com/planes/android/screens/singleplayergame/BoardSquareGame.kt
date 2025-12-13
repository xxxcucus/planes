package com.planes.android.screens.singleplayergame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.planes.singleplayerengine.GuessPoint

@Composable
fun BoardSquareGame(index: Int, squareSizeDp: Int, squareSizePx: Float,
                    planesGridViewModel: PlaneGridViewModel,
                    onClick: (Int) -> Unit) {
    val row = index / planesGridViewModel.getColNo()
    val col = index % planesGridViewModel.getColNo()

    val pointOnPlane = planesGridViewModel.isPointOnPlane(col, row)

    val guess = planesGridViewModel.getGuessAtPosition(col, row)

   if (!pointOnPlane.first)
       GridSquareGame(
           isComputer = planesGridViewModel.isComputer(),
           annotation = 0,
           guess = guess,
           widthDp = squareSizeDp,
           widthPx = squareSizePx,
           backgroundColor = Color.White,
           index = index,
           onClick = onClick
       )
    else {
       val annotation = planesGridViewModel.getAnnotation(pointOnPlane.second)
       val planesIdx = planesGridViewModel.decodeAnnotation(annotation)

       if (planesIdx.size == 1) {
           GridSquareGame(
               isComputer = planesGridViewModel.isComputer(),
               annotation = if (planesIdx[0] < 0) -2 else planesIdx[0] + 1,
               guess = guess,
               widthDp = squareSizeDp,
               widthPx = squareSizePx,
               backgroundColor = Color.Blue,
               index = index,
               onClick = onClick
           )
           //Log.d("Planes", "plane ${planesIdx[0]}")
       } else {
           GridSquareGame(
               isComputer = planesGridViewModel.isComputer(),
               annotation = -1,
               guess = guess,
               widthDp = squareSizeDp,
               widthPx = squareSizePx,
               backgroundColor = Color.Blue,
               index = index,
               onClick = onClick
           )
       }
   }
}