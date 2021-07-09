package com.planes.common;

import java.util.Iterator;

public class PlaneIntersectingPointIterator extends VectorIterator<Plane> {

    public PlaneIntersectingPointIterator(final Coordinate2D point) {
        m_point = (Coordinate2D)point.clone();
        generateList();
    }


    private void generateList() {
        m_internalList.clear();

        //build a list of all possible positions that can possibly contain the (0,0) point
        //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
        for(int i = -5 + m_point.x(); i < 6 + m_point.x(); i++)
            for(int j = -5 + m_point.y(); j < 6 + m_point.y(); j++)
                for(int k = 0; k < 4; k++)
                {
                    Plane pl = new Plane(i, j, Orientation.values()[k]);
                    m_internalList.add(pl);
                }

        Iterator<Plane> it = m_internalList.iterator();

        //elimintate all positions that do not contain (0,0)
        while (it.hasNext())
        {
            Plane pl = it.next();
            if (!pl.containsPoint(m_point))
            {
                it.remove();
            }
        }
    }

    private Coordinate2D m_point;
}
