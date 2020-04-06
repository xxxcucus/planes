package com.planes.common;

public class PointInfluenceIterator extends VectorIterator<Coordinate2D> {

    public PointInfluenceIterator(final Coordinate2D point) {
        m_point = (Coordinate2D)point.clone();
        generateList();
    }


    private void generateList() {
        m_internalList.clear();

        for (int i = 0; i < 4; i++) {
            Plane pl = new Plane(m_point.x(), m_point.y(), Orientation.values()[i]);
            PlanePointIterator ppi = new PlanePointIterator(pl);

            while (ppi.hasNext()) {
                Coordinate2D point = ppi.next();
                if (m_internalList.indexOf(point) < 0)
                    m_internalList.add(point);
            }
        }
    }

    private Coordinate2D m_point;
}
