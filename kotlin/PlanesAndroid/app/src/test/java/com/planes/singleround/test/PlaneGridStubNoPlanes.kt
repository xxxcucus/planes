package com.planes.singleround.test.com.planes.singleround.test
import com.planes.single_player_engine.PlaneGrid

internal class PlaneGridStubNoPlanes(row: Int, col: Int, planesNo: Int, isComputer: Boolean) : PlaneGrid(row, col, planesNo, isComputer) {
    override fun initGrid() {}
}