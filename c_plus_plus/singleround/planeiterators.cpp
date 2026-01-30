#include "planeiterators.h"
#include "planepointiterator.h"
#include "coordinate2d.h"
#include <algorithm>

/*
//constructor
PlanePointIterator::PlanePointIterator(const Plane& pl):
    PlanesCommonTools::VectorIterator<PlanesCommonTools::Coordinate2D>(),
    m_plane(pl)
{
    generateList();
}

//the function that generates the list of points
void PlanePointIterator::generateList()
{

    const PlanesCommonTools::Coordinate2D pointsNorthSouth[] = {PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(0, 1), PlanesCommonTools::Coordinate2D(-1, 1),
                                                                PlanesCommonTools::Coordinate2D(1, 1), PlanesCommonTools::Coordinate2D(-2, 1), PlanesCommonTools::Coordinate2D(2, 1),
                                                                PlanesCommonTools::Coordinate2D(0, 2),PlanesCommonTools::Coordinate2D(0, 3), PlanesCommonTools::Coordinate2D(-1, 3),
                                                                PlanesCommonTools::Coordinate2D(1, 3)};

    const PlanesCommonTools::Coordinate2D pointsSouthNorth[] = {PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(0, -1), PlanesCommonTools::Coordinate2D(-1, -1),
                                                                PlanesCommonTools::Coordinate2D(1, -1), PlanesCommonTools::Coordinate2D(-2, -1), PlanesCommonTools::Coordinate2D(2, -1),
                                                                PlanesCommonTools::Coordinate2D(0, -2), PlanesCommonTools::Coordinate2D(0, -3), PlanesCommonTools::Coordinate2D(-1, -3),
                                                                PlanesCommonTools::Coordinate2D(1, -3)};

    const PlanesCommonTools::Coordinate2D pointsEastWest[] = {PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(1, 0), PlanesCommonTools::Coordinate2D(1, -1),
                                                              PlanesCommonTools::Coordinate2D(1, 1), PlanesCommonTools::Coordinate2D(1, -2), PlanesCommonTools::Coordinate2D(1, 2),
                                                              PlanesCommonTools::Coordinate2D(2, 0), PlanesCommonTools::Coordinate2D(3, 0), PlanesCommonTools::Coordinate2D(3, -1),
                                                              PlanesCommonTools::Coordinate2D(3, 1)};

    const PlanesCommonTools::Coordinate2D pointsWestEast[] = {PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(-1, 0), PlanesCommonTools::Coordinate2D(-1, -1),
                                                              PlanesCommonTools::Coordinate2D(-1, 1), PlanesCommonTools::Coordinate2D(-1, -2), PlanesCommonTools::Coordinate2D(-1, 2),
                                                              PlanesCommonTools::Coordinate2D(-2, 0), PlanesCommonTools::Coordinate2D(-3, 0), PlanesCommonTools::Coordinate2D(-3, 1),
                                                              PlanesCommonTools::Coordinate2D(-3, -1)};

    const int size = 10;
    for(int i = 0; i < size; ++i)
    {
        switch(m_plane.orientation())
        {
            case Plane::NorthSouth:
                m_internalList.push_back(pointsNorthSouth[i] + m_plane.head());
                break;
            case Plane::SouthNorth:
                m_internalList.push_back(pointsSouthNorth[i] + m_plane.head());
                break;
            case Plane::WestEast:
                m_internalList.push_back(pointsWestEast[i] + m_plane.head());
                break;
            case Plane::EastWest:
                m_internalList.push_back(pointsEastWest[i] + m_plane.head());
                break;
            default:
                ;
        }
    }
} */

//constructor for the iterator giving all the planes
//passing through the point (0,0)
/*PlaneIntersectingPointIterator::PlaneIntersectingPointIterator(const PlanesCommonTools::Coordinate2D& qp) :
    PlanesCommonTools::VectorIterator<Plane>(),
    m_point(qp)
{
    //generates the list of planes
    generateList();
}

//builds the list of planes that intersect (0,0)
void PlaneIntersectingPointIterator::generateList()
{
    m_internalList.clear();

    //build a list of all possible positions that can possibly contain the (0,0) point
    //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
    for(int i = -5 + m_point.x(); i < 6 + m_point.x(); i++)
        for(int j = -5 + m_point.y(); j < 6 + m_point.y(); j++)
            for(int k = 0; k < 4; k++)
            {
                Plane pl(i, j, (Plane::Orientation)k);
                m_internalList.push_back(pl);
            }

    auto it = m_internalList.begin();

    //elimintate all positions that do not contain (0,0)
    while(it != m_internalList.end())
    {
        if (!it->containsPoint(m_point))
        {
            it = m_internalList.erase(it);
            continue;
        }
        ++it;
    }
}*/

PointInfluenceIterator::PointInfluenceIterator(const PlanesCommonTools::Coordinate2D& qp):
    PlanesCommonTools::VectorIterator<PlanesCommonTools::Coordinate2D>(),
    m_point(qp)
{
    generateList();
}

void PointInfluenceIterator::generateList()
{
    m_internalList.clear();

	for (int i = 0; i < 4; i++) {
		Plane pl(m_point.x(), m_point.y(), (Plane::Orientation)i);
		PlanePointIterator ppi(pl);

		while (ppi.hasNext()) {
			PlanesCommonTools::Coordinate2D point = ppi.next();
			if (std::find(m_internalList.begin(), m_internalList.end(), point) == m_internalList.end())
				m_internalList.push_back(point);
		}
	}
}
