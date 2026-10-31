package com.planes.android.singleroundtest
import com.planes.singleplayerengine.PlaneGrid

internal class PlaneGridStubNoPlanes(row: Int, col: Int, planesNo: Int, isComputer: Boolean) : PlaneGrid(row, col, planesNo, isComputer) {
    override fun initGrid() {}
}