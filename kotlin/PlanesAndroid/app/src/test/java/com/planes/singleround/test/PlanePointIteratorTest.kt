package com.planes.singleround.test.com.planes.singleround.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.Coordinate2D
import com.planes.single_player_engine.Orientation
import com.planes.single_player_engine.Plane
import com.planes.single_player_engine.PlanePointIterator
import org.junit.Test
import java.util.*

class PlanePointIteratorTest {
    @Test
    fun PlanePointIterator_GenerateListNorthSouth() {
        val pl = Plane(0, 0, Orientation.NorthSouth)
        val ppi = PlanePointIterator(pl)
        val planePoints = Vector<Coordinate2D?>()
        val yCoords = Vector<Int>()
        while (ppi.hasNext()) {
            val c = ppi.next()
            planePoints.add(c)
            yCoords.add(c.y())
        }
        Truth.assertThat(planePoints.size == 10).isTrue()
        val histoY = computeHisto(yCoords)
        Truth.assertThat(histoY[0] == 1).isTrue()
        Truth.assertThat(histoY[1] == 5).isTrue()
        Truth.assertThat(histoY[2] == 1).isTrue()
        Truth.assertThat(histoY[3] == 3).isTrue()
    }

    @Test
    fun PlanePointIterator_GenerateListSouthNorth() {
        val pl = Plane(0, 0, Orientation.SouthNorth)
        val ppi = PlanePointIterator(pl)
        val planePoints = Vector<Coordinate2D?>()
        val yCoords = Vector<Int>()
        while (ppi.hasNext()) {
            val c = ppi.next()
            planePoints.add(c)
            yCoords.add(c.y())
        }
        Truth.assertThat(planePoints.size == 10).isTrue()
        val histoY = computeHisto(yCoords)
        Truth.assertThat(histoY[0] == 1).isTrue()
        Truth.assertThat(histoY[-1] == 5).isTrue()
        Truth.assertThat(histoY[-2] == 1).isTrue()
        Truth.assertThat(histoY[-3] == 3).isTrue()
    }

    @Test
    fun PlanePointIterator_GenerateListEastWest() {
        val pl = Plane(0, 0, Orientation.EastWest)
        val ppi = PlanePointIterator(pl)
        val planePoints = Vector<Coordinate2D?>()
        val xCoords = Vector<Int>()
        while (ppi.hasNext()) {
            val c = ppi.next()
            planePoints.add(c)
            xCoords.add(c.x())
        }
        Truth.assertThat(planePoints.size == 10).isTrue()
        val histoX = computeHisto(xCoords)
        Truth.assertThat(histoX[0] == 1).isTrue()
        Truth.assertThat(histoX[1] == 5).isTrue()
        Truth.assertThat(histoX[2] == 1).isTrue()
        Truth.assertThat(histoX[3] == 3).isTrue()
    }

    @Test
    fun PlanePointIterator_GenerateListWestEast() {
        val pl = Plane(0, 0, Orientation.WestEast)
        val ppi = PlanePointIterator(pl)
        val planePoints = Vector<Coordinate2D?>()
        val xCoords = Vector<Int>()
        while (ppi.hasNext()) {
            val c = ppi.next()
            planePoints.add(c)
            xCoords.add(c.x())
        }
        Truth.assertThat(planePoints.size == 10).isTrue()
        val histoX = computeHisto(xCoords)
        Truth.assertThat(histoX[0] == 1).isTrue()
        Truth.assertThat(histoX[-1] == 5).isTrue()
        Truth.assertThat(histoX[-2] == 1).isTrue()
        Truth.assertThat(histoX[-3] == 3).isTrue()
    }

    private fun computeHisto(values: Vector<Int>): HashMap<Int, Int> {
        val retVal = HashMap<Int, Int>()
        for (value in values) {
            if (retVal.containsKey(value)) {
                retVal[value] = retVal[value]!! + 1
            } else {
                retVal[value] = 1
            }
        }
        return retVal
    }
}