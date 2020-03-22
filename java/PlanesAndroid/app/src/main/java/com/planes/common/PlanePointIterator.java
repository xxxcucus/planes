package com.planes.common;

public class PlanePointIterator extends VectorIterator<Coordinate2D> {

    public PlanePointIterator(final Plane pl) {
        m_plane = (Plane)pl.clone();
        generateList();
    }

    private void generateList() {
    final Coordinate2D pointsNorthSouth[] = new Coordinate2D[]  {new Coordinate2D(0, 0), new Coordinate2D(0, 1), new Coordinate2D(-1, 1),
            new Coordinate2D(1, 1), new Coordinate2D(-2, 1), new Coordinate2D(2, 1),
            new Coordinate2D(0, 2), new Coordinate2D(0, 3), new Coordinate2D(-1, 3),
            new Coordinate2D(1, 3)};

    final Coordinate2D pointsSouthNorth[] = new Coordinate2D[] { new Coordinate2D(0, 0), new Coordinate2D(0, -1), new Coordinate2D(-1, -1),
            new Coordinate2D(1, -1), new Coordinate2D(-2, -1), new Coordinate2D(2, -1),
            new Coordinate2D(0, -2), new Coordinate2D(0, -3), new Coordinate2D(-1, -3),
            new Coordinate2D(1, -3)};

    final Coordinate2D pointsEastWest[] = new Coordinate2D[] { new Coordinate2D(0, 0), new Coordinate2D(1, 0), new Coordinate2D(1, -1),
            new Coordinate2D(1, 1), new Coordinate2D(1, -2), new Coordinate2D(1, 2),
            new Coordinate2D(2, 0), new Coordinate2D(3, 0), new Coordinate2D(3, -1),
            new Coordinate2D(3, 1)};

    final Coordinate2D pointsWestEast[] = new Coordinate2D[] { new Coordinate2D(0, 0), new Coordinate2D(-1, 0), new Coordinate2D(-1, -1),
            new Coordinate2D(-1, 1), new Coordinate2D(-1, -2), new Coordinate2D(-1, 2),
            new Coordinate2D(-2, 0), new Coordinate2D(-3, 0), new Coordinate2D(-3, 1),
            new Coordinate2D(-3, -1)};

    final int size = 10;
        for(int i = 0; i < size; ++i)
        {
            switch(m_plane.orientation())
            {
                case NorthSouth:
                    m_internalList.add(pointsNorthSouth[i].add(m_plane.head()));
                    break;
                case SouthNorth:
                    m_internalList.add(pointsSouthNorth[i].add(m_plane.head()));
                    break;
                case WestEast:
                    m_internalList.add(pointsWestEast[i].add(m_plane.head()));
                    break;
                case EastWest:
                    m_internalList.add(pointsEastWest[i].add(m_plane.head()));
                    break;
                default:
                    ;
            }
        }
    }

    private Plane m_plane;
}
