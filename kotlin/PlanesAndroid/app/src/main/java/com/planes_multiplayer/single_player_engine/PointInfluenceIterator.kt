package com.planes_multiplayer.single_player_engine

class PointInfluenceIterator(point: Coordinate2D) : VectorIterator<Coordinate2D>() {
    private fun generateList() {
        m_internalList.clear()
        for (i in 0..3) {
            val pl = Plane(m_point.x(), m_point.y(), Orientation.values()[i])
            val ppi = PlanePointIterator(pl)
            while (ppi.hasNext()) {
                val point = ppi.next()
                if (m_internalList.indexOf(point) < 0) m_internalList.add(point)
            }
        }
    }

    private val m_point: Coordinate2D

    init {
        m_point = point.clone() as Coordinate2D
        generateList()
    }
}