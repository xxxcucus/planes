import com.planes_multiplayer.single_player_engine.PlaneGrid

internal class PlaneGridStubNoPlanes(row: Int, col: Int, planesNo: Int, isComputer: Boolean) : PlaneGrid(row, col, planesNo, isComputer) {
    override fun initGrid() {}
}