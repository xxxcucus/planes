package com.planes.common;

public class PointInfluenceIterator extends VectorIterator<Coordinate2D> {

    public PointInfluenceIterator(final Coordinate2D point) {
        m_point = (Coordinate2D)point.clone();
        generateList();
    }


    private void generateList() {
        m_internalList.clear();

        //searches in a range around the selected QPoint
        for(int i = -10 + m_point.x(); i < 10 + m_point.y(); i++)
            for(int j = -10 + m_point.y(); j < 10 + m_point.y(); j++)
            {
                Coordinate2D qp = new Coordinate2D(i, j);
                //generates all planes intersecting the point
                PlaneIntersectingPointIterator pipi = new PlaneIntersectingPointIterator(qp);

                boolean pointFound = false;

                //check to see if any of these planes correspond to the initial point
                while(pipi.hasNext())
                {
                    Plane pl = pipi.next();
                    if(pl.isHead(m_point))
                    {
                        pointFound = true;
                        break;
                    }
                }

                if(pointFound)
                {
                    m_internalList.add(qp);
                    continue;
                }
            }
    }

    private Coordinate2D m_point;
}
