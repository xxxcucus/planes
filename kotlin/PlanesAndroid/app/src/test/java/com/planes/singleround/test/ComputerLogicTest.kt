package com.planes.singleround.test.com.planes.singleround.test

import com.google.common.truth.Truth
import com.planes.single_player_engine.ComputerLogic
import com.planes.single_player_engine.PlaneGrid
import com.planes.single_player_engine.Plane
import com.planes.single_player_engine.Orientation
import com.planes.single_player_engine.HeadData
import com.planes.single_player_engine.Coordinate2D
import org.junit.Test

class ComputerLogicTest {
    @Test
    fun ComputerLogic_MapIndexToPlane() {
        val cl = ComputerLogic(10, 10, 3)
        Truth.assertThat(cl.mapPlaneToIndex(cl.mapIndexToPlane(50))).isEqualTo(50)
    }

    @Test
    fun ComputerLogic_UpdateChoiceMapMissInfo() {

        //find a plane that passes through (5, 5)
        var grid = PlaneGrid(10, 10, 3, false)
        var pl = Plane(4, 5, Orientation.EastWest)
        grid.savePlane(pl)
        grid.computePlanePointsList()
        Truth.assertThat(grid.isPointOnPlane(5, 5).first).isTrue()

        var cl = ComputerLogic(10, 10, 3)
        cl.updateChoiceMapMissInfo(5, 5)
        var index = cl.mapPlaneToIndex(pl)
        Truth.assertThat(cl.choicesArray[index]).isEqualTo(-1)
    }

    @Test
    fun ComputerLogic_UpdateChoiceMapHitInfo() {
        //find a plane that passes through (5, 5)
        var grid = PlaneGrid(10, 10, 3, false)
        var pl = Plane(4, 5, Orientation.EastWest)
        grid.savePlane(pl)
        grid.computePlanePointsList()
        Truth.assertThat(grid.isPointOnPlane(5, 5).first).isTrue()

        var cl = ComputerLogic(10, 10, 3)
        var index = cl.mapPlaneToIndex(pl)
        cl.choicesArray[index] = 1
        cl.updateChoiceMapHitInfo(5, 5)
        Truth.assertThat(cl.choicesArray[index]).isEqualTo(2)
    }

    @Test
    fun ComputerLogic_UpdateChoiceMapPlaneData() {
        var cl = ComputerLogic(10, 10, 3)
        var pl = Plane(4, 5, Orientation.EastWest)
        cl.updateChoiceMapPlaneData(pl)

        var grid = PlaneGrid(10, 10, 3, false)
        grid.savePlane(pl)
        grid.computePlanePointsList()

        for ( gp in cl.m_extendedListGuesses) {
            Truth.assertThat(grid.isPointOnPlane(gp.m_row, gp.m_col).first).isTrue()
        }
    }

    @Test
    fun ComputerLogic_MakeChoiceFindHeadModeMaxExists() {

        var cl = ComputerLogic(10, 10, 3)
        var pl = Plane(4, 5, Orientation.EastWest)
        var index = cl.mapPlaneToIndex(pl)

        cl.choicesArray[index] = 1
        var result = cl.makeChoiceFindHeadMode()

        Truth.assertThat(result.second).isEqualTo(Coordinate2D(4, 5))
    }

    @Test
    fun ComputerLogic_MakeChoiceFindHeadModeMaxDoesNotExist() {
        var cl = ComputerLogic(10, 10, 3);

        for (i in 0..cl.choicesArray.size - 1) {
            cl.choicesArray[i] = -1
        }
        Truth.assertThat(cl.makeChoiceFindHeadMode().first).isFalse()
    }

    @Test
    fun ComputerLogic_MakeChoiceFindPositionModeChoiceDoesNotExist() {
        var cl = ComputerLogic(10, 10, 3)
        var hd = HeadData(10, 10, 5, 5)
        for (i in 0..3)
            hd.m_options[i].m_discarded = true
        cl.m_headDataList.add(hd)

        Truth.assertThat(cl.makeChoiceFindPositionMode().first).isFalse()
    }

    @Test
    fun ComputerLogic_MakeChoiceFindPositionModeChoiceDoesExist() {
        var cl = ComputerLogic(10, 10, 3)
        var hd = HeadData(10, 10, 5, 5)
        for (i in 0..2)
            hd.m_options[i].m_discarded = true
        cl.m_headDataList.add(hd)


        var coords = cl.makeChoiceFindPositionMode().second

        var notTested = hd.m_options[3].m_pointsNotTested
        var index = notTested.indexOf(coords)
        Truth.assertThat(index >= 0).isTrue()
    }

    @Test
    fun ComputerLogic_MakeChoiceRandomModeChoiceDoesExist() {
        var cl = ComputerLogic(10, 10, 3)
        var result = cl.makeChoiceRandomMode()

        var found = false

        for (i in 0..3) {
            var pl = Plane(result.second.x(), result.second.y(), Orientation.fromInt(i))
            var index = cl.mapPlaneToIndex(pl)
            if (cl.choicesArray[index] == 0)
                found = true
        }

        Truth.assertThat(found).isTrue()
    }

    @Test
    fun ComputerLogic_MakeChoiceRandomModeChoiceDoesNotExist() {
        var cl = ComputerLogic(10, 10, 3)
        for (i in 0..cl.choicesArray.size - 1) {
            cl.choicesArray[i] = -1
        }

        Truth.assertThat(cl.makeChoiceRandomMode().first).isFalse()
    }
}