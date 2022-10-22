package com.planes.android.game.common

interface GameBoardInterface {
    fun touchEventUp(row: Int, col: Int, row_diff: Int, col_diff: Int, touchedTime: Long)
}
