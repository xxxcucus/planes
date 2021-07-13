package com.planes.common

import com.planes.common.Plane
import com.planes.common.PlaneOrientationData
import com.planes.common.GuessPoint

class HeadData(//size of the grid
        var m_row: Int, var m_col: Int, //position of the head
        var m_headRow: Int, var m_headCol: Int) {
    //the correct plane orientation if decided
    var m_correctOrient: Int

    //statistics about the 4 positions with this head
    var m_options: Array<PlaneOrientationData>

    init {
        m_correctOrient = -1
        m_options = Array(4) { i -> PlaneOrientationData()  }
        for (i in 0..3) {
            val pl = Plane(m_headRow, m_headCol, Orientation.values()[i])
            //create the four planes for each head position
            m_options[i] = PlaneOrientationData(pl, false)
            if (!pl.isPositionValid(m_row, m_col)) m_options[i].m_discarded = true
        }
    }

    //update the current data with a guess
    //return true if a plane is confirmed
    fun update(gp: GuessPoint): Boolean {
        //if the head data is already conclusive ignore
        if (m_correctOrient != -1) return true

        //update the four plane positions with this new data
        for (i in 0..3) m_options[i].update(gp)

        //verify if we checked all points of a plane
        for (i in 0..3) {
            if (!m_options[i].m_discarded && m_options[i].areAllPointsChecked()) {
                m_correctOrient = i
                return true
            }
        }

        //verify if 3 of the 4 possible orientations are discarded
        var count = 0
        var good_orientation = -1
        for (i in 0..3) {
            if (m_options[i].m_discarded) count++ else good_orientation = i
        }
        //if there are exactly 3 wrong orientations
        if (count == 3) {
            m_correctOrient = good_orientation
            return true
        }
        return false
    }


}