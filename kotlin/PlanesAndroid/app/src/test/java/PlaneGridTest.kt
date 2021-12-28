package com.planes.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.*
import org.junit.Test
import java.util.*

class PlaneGridTest {
    @Test
    fun PlaneGridTest_SaveSearchPlane() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl1 = Plane(rows / 2, 0, Orientation.NorthSouth)
        Truth.assertThat(pl1.isPositionValid(rows, cols)).isTrue()
        val pl2 = Plane(0, 6, Orientation.EastWest)
        Truth.assertThat(pl2.isPositionValid(rows, cols)).isTrue()
        val pl3 = Plane(6, 6, Orientation.EastWest)
        Truth.assertThat(pl3.isPositionValid(rows, cols)).isTrue()
        grid.savePlane(pl1)
        grid.savePlane(pl2)
        grid.savePlane(pl3)
        Truth.assertThat(grid.searchPlane(pl1) == 0).isTrue()
        Truth.assertThat(grid.searchPlane(pl2) == 1).isTrue()
        Truth.assertThat(grid.searchPlane(pl3) == 2).isTrue()
        Truth.assertThat(grid.searchPlane(0, 0) >= 0).isFalse()
        Truth.assertThat(grid.searchPlane(pl1.row(), pl1.col()) == 0).isTrue()
        Truth.assertThat(grid.searchPlane(pl2.row(), pl2.col()) == 1).isTrue()
        Truth.assertThat(grid.searchPlane(pl3.row(), pl3.col()) == 2).isTrue()
    }

    @Test
    fun PlaneGridTest_RemoveSearchPlane() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl1 = Plane(rows / 2, 0, Orientation.NorthSouth)
        Truth.assertThat(pl1.isPositionValid(rows, cols)).isTrue()
        val pl2 = Plane(0, 6, Orientation.EastWest)
        Truth.assertThat(pl2.isPositionValid(rows, cols)).isTrue()
        val pl3 = Plane(6, 6, Orientation.EastWest)
        Truth.assertThat(pl3.isPositionValid(rows, cols)).isTrue()
        grid.savePlane(pl1)
        grid.savePlane(pl2)
        grid.savePlane(pl3)
        val result = grid.removePlane(0)
        Truth.assertThat(result.first).isTrue()
        Truth.assertThat(result.second.equals(pl1)).isTrue()
        Truth.assertThat(grid.searchPlane(pl2) == 0).isTrue()
        Truth.assertThat(grid.searchPlane(pl3) == 1).isTrue()
        Truth.assertThat(grid.searchPlane(pl1) >= 0).isFalse()
        grid.removePlane(pl2)
        Truth.assertThat(grid.searchPlane(pl2) >= 0).isFalse()
        Truth.assertThat(grid.searchPlane(pl3) == 0).isTrue()
    }

    @Test
    fun PlaneGridTest_isPointOnPlane() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        grid.setPlanePoints(Vector(Arrays.asList(*arrayOf(Coordinate2D(0, 0), Coordinate2D(0, 1)))))
        Truth.assertThat(grid.isPointOnPlane(0, 0).first).isTrue()
        Truth.assertThat(grid.isPointOnPlane(0, 0).second == 0).isTrue()
        Truth.assertThat(grid.isPointOnPlane(0, 1).first).isTrue()
        Truth.assertThat(grid.isPointOnPlane(0, 1).second == 1).isTrue()
        Truth.assertThat(grid.isPointOnPlane(0, 2).first).isFalse()
    }

    @Test
    fun PlaneGridTest_computePlanePointListNoOverlapAllInsideGrid() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl_list = arrayOf(Plane(rows / 2, 0, Orientation.NorthSouth),
                Plane(0, 6, Orientation.EastWest), Plane(6, 6, Orientation.EastWest))
        grid.savePlane(pl_list[0])
        grid.savePlane(pl_list[1])
        grid.savePlane(pl_list[2])
        grid.computePlanePointsList()
        for (i in 0..2) {
            val ppi = PlanePointIterator(pl_list[i])
            while (ppi.hasNext()) {
                val point = ppi.next()
                Truth.assertThat(grid.isPointOnPlane(point.x(), point.y()).first).isTrue()
            }
        }
    }

    @Test
    fun PlaneGridTest_computePlanePointListOutsideGrid() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl_list = arrayOf(Plane(rows / 2, 0, Orientation.NorthSouth),
                Plane(0, 6, Orientation.WestEast), Plane(6, 6, Orientation.EastWest))
        grid.savePlane(pl_list[0])
        grid.savePlane(pl_list[1])
        grid.savePlane(pl_list[2])
        grid.computePlanePointsList()
        Truth.assertThat(grid.isPlaneOutsideGrid).isTrue()
    }

    @Test
    fun PlaneGridTest_computePlanePointListPlanesOverlap() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl_list = arrayOf(Plane(rows / 2, 0, Orientation.NorthSouth),
                Plane(0, 6, Orientation.EastWest), Plane(6, 6, Orientation.WestEast))
        grid.savePlane(pl_list[0])
        grid.savePlane(pl_list[1])
        grid.savePlane(pl_list[2])
        Truth.assertThat(grid.computePlanePointsList()).isFalse()
    }

    @Test
    fun PlaneGridTest_generateAnnotation() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        Truth.assertThat(grid.generateAnnotation(0, false)).isEqualTo(1)
        Truth.assertThat(grid.generateAnnotation(0, true)).isEqualTo(2)
    }

    @Test
    fun PlaneGridTest_getGuessResult() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl_list = arrayOf(Plane(rows / 2, 0, Orientation.NorthSouth),
                Plane(0, 6, Orientation.EastWest), Plane(6, 6, Orientation.EastWest))
        grid.savePlane(pl_list[0])
        grid.savePlane(pl_list[1])
        grid.savePlane(pl_list[2])
        grid.computePlanePointsList()
        Truth.assertThat(grid.getGuessResult(Coordinate2D(rows / 2, 0))).isEqualTo(Type.Dead)
        Truth.assertThat(grid.getGuessResult(Coordinate2D(rows / 2, 1))).isEqualTo(Type.Hit)
        Truth.assertThat(grid.getGuessResult(Coordinate2D(rows / 2 + 1, 0))).isEqualTo(Type.Miss)
    }

    @Test
    fun PlaneGridTest_decodeAnnotation() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        Truth.assertThat(grid.decodeAnnotation(1)).isEqualTo(Vector(Arrays.asList(*arrayOf(0))))
        Truth.assertThat(grid.decodeAnnotation(2)).isEqualTo(Vector(Arrays.asList(*arrayOf(-1))))
        Truth.assertThat(grid.decodeAnnotation(5)).isEqualTo(Vector(Arrays.asList(*arrayOf(0, 1))))
        Truth.assertThat(grid.decodeAnnotation(9)).isEqualTo(Vector(Arrays.asList(*arrayOf(0, -2))))
    }

    @Test
    fun PlaneGridTest_addGuessPoints() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        grid.addGuess(GuessPoint(0, 0, Type.Hit))
        val guesses = grid.guesses
        Truth.assertThat(guesses).containsExactly(GuessPoint(0, 0, Type.Hit))
    }

    @Test
    fun PlaneGridTest_getPlanePoints() {
        val rows = 10
        val cols = 10
        val grid = PlaneGridStubNoPlanes(rows, cols, 3, false)
        val pl_list = arrayOf(Plane(rows / 2, 0, Orientation.NorthSouth),
                Plane(0, 6, Orientation.EastWest), Plane(6, 6, Orientation.EastWest))
        grid.savePlane(pl_list[0])
        grid.savePlane(pl_list[1])
        grid.savePlane(pl_list[2])
        val planePointsResult = grid.getPlanePoints(-1)
        Truth.assertThat(planePointsResult.second.size == 0).isTrue()
        Truth.assertThat(planePointsResult.first).isFalse()
        val planePointsResult1 = grid.getPlanePoints(0)
        Truth.assertThat(planePointsResult1.second.size == 9).isTrue()
        Truth.assertThat(planePointsResult1.first).isTrue()
    }
}