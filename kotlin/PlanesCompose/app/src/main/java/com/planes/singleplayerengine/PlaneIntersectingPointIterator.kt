package com.planes.singleplayerengine

class PlaneIntersectingPointIterator(point: Coordinate2D) : VectorIterator<Plane>() {

    private val m_point: Coordinate2D

    init {
        m_point = point.clone() as Coordinate2D
        generateList()
    }

    private fun generateList() {
        m_internalList.clear()

        //build a list of all possible positions that can possibly contain the (0,0) point
        //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
        for (i in -5 + m_point.x() until 6 + m_point.x()) for (j in -5 + m_point.y() until 6 + m_point.y()) for (k in 0..3) {
            val pl = Plane(i, j, Orientation.values()[k])
            m_internalList.add(pl)
        }
        val it = m_internalList.iterator()

        //eliminate all positions that do not contain (0,0)
        while (it.hasNext()) {
            val pl = it.next()
            if (!pl!!.containsPoint(m_point)) {
                it.remove()
            }
        }
    }
}