package com.planes.common

class PlanePointIterator(pl: Plane) : VectorIterator<Coordinate2D>() {
    private val m_plane: Plane

    init {
        m_plane = pl.clone() as Plane
        generateList()
    }

    private fun generateList() {
        val pointsNorthSouth = arrayOf(Coordinate2D(0, 0), Coordinate2D(0, 1), Coordinate2D(-1, 1),
                Coordinate2D(1, 1), Coordinate2D(-2, 1), Coordinate2D(2, 1),
                Coordinate2D(0, 2), Coordinate2D(0, 3), Coordinate2D(-1, 3),
                Coordinate2D(1, 3))
        val pointsSouthNorth = arrayOf(Coordinate2D(0, 0), Coordinate2D(0, -1), Coordinate2D(-1, -1),
                Coordinate2D(1, -1), Coordinate2D(-2, -1), Coordinate2D(2, -1),
                Coordinate2D(0, -2), Coordinate2D(0, -3), Coordinate2D(-1, -3),
                Coordinate2D(1, -3))
        val pointsEastWest = arrayOf(Coordinate2D(0, 0), Coordinate2D(1, 0), Coordinate2D(1, -1),
                Coordinate2D(1, 1), Coordinate2D(1, -2), Coordinate2D(1, 2),
                Coordinate2D(2, 0), Coordinate2D(3, 0), Coordinate2D(3, -1),
                Coordinate2D(3, 1))
        val pointsWestEast = arrayOf(Coordinate2D(0, 0), Coordinate2D(-1, 0), Coordinate2D(-1, -1),
                Coordinate2D(-1, 1), Coordinate2D(-1, -2), Coordinate2D(-1, 2),
                Coordinate2D(-2, 0), Coordinate2D(-3, 0), Coordinate2D(-3, 1),
                Coordinate2D(-3, -1))
        val size = 10
        for (i in 0 until size) {
            when (m_plane.orientation()) {
                Orientation.NorthSouth -> m_internalList.add(pointsNorthSouth[i].add(m_plane.head()))
                Orientation.SouthNorth -> m_internalList.add(pointsSouthNorth[i].add(m_plane.head()))
                Orientation.WestEast -> m_internalList.add(pointsWestEast[i].add(m_plane.head()))
                Orientation.EastWest -> m_internalList.add(pointsEastWest[i].add(m_plane.head()))
            }
        }
    }
}