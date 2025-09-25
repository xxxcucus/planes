package com.planes.singleplayerengine

import java.util.*

class PlaneOrientationData {
    //the position of the plane
    private var m_plane: Plane

    //whether this orientation was discarded
    var m_discarded: Boolean

    //points on this plane that were not tested
    //if m_discarded is false it means that all the
    //tested points were hits
    var m_pointsNotTested: Vector<Coordinate2D>

    //default constructor
    constructor() {
        m_plane = Plane(0, 0, Orientation.values()[0])
        m_discarded = true
        m_pointsNotTested = Vector()
    }

    //another constructor
    constructor(pl: Plane, isDiscarded: Boolean) {
        m_plane = pl.clone() as Plane
        m_discarded = isDiscarded
        m_pointsNotTested = Vector()
        val ppi = PlanePointIterator(m_plane)
        ppi.next()
        while (ppi.hasNext()) {
            m_pointsNotTested.add(ppi.next().clone() as Coordinate2D)
        }
    }

    //update the info about this plane with another guess point
    //a guess point is a pair (position, guess result)
    fun update(gp: GuessPoint) {
        //if plane is discarded return
        if (m_discarded) return

        //find the guess point in the list of points not tested
        val idx = m_pointsNotTested.indexOf(Coordinate2D(gp.row(), gp.col()))

        //if point not found return
        if (idx < 0) return

        //if point found
        //if dead and point is the planes head remove the head from the list of untested points
        //TODO: the head point is not in the list so this is useless
        if (gp.type() === Type.Dead && m_plane.isHead(Coordinate2D(gp.row(), gp.col()))) {
            m_pointsNotTested.removeAt(idx)
            return
        }

        //if miss or dead discard plane
        if (gp.type() === Type.Miss || gp.type() === Type.Dead) m_discarded = true

        //if hit take point out of the list of points not tested
        if (gp.type() === Type.Hit) m_pointsNotTested.removeAt(idx)
    }

    //verifies if all the points in the current orientation were already checked
    fun areAllPointsChecked(): Boolean {
        return m_pointsNotTested.isEmpty()
    }
}