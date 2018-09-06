#include "planeiterators.h"
#include "coordinate2d.h"

//constructor
PlanePointIterator::PlanePointIterator(const Plane& pl):
    PlanesCommonTools::ListIterator<PlanesCommonTools::Coordinate2D>(),
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
                m_internalList << pointsNorthSouth[i] + m_plane.head();
                break;
            case Plane::SouthNorth:
                m_internalList << pointsSouthNorth[i] + m_plane.head();
                break;
            case Plane::WestEast:
                m_internalList << pointsWestEast[i] + m_plane.head();
                break;
            case Plane::EastWest:
                m_internalList << pointsEastWest[i] + m_plane.head();
                break;
            default:
                ;
        }
    }
}

//constructor for the iterator giving all the planes
//passing through the point (0,0)
PlaneIntersectingPointIterator::PlaneIntersectingPointIterator(const PlanesCommonTools::Coordinate2D& qp):
    PlanesCommonTools::ListIterator<Plane>(),
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
                m_internalList.append(pl);
            }

    QMutableListIterator<Plane> i(m_internalList);

    //elimintate all positions that do not contain (0,0)
    while(i.hasNext())
    {
        Plane pl = i.next();

        if(!pl.containsPoint(m_point))
        {
            i.remove();
            continue;
        }
    }
}

PointInfluenceIterator::PointInfluenceIterator(const PlanesCommonTools::Coordinate2D& qp):
    PlanesCommonTools::ListIterator<PlanesCommonTools::Coordinate2D>(),
    m_point(qp)
{
    generateList();
}

void PointInfluenceIterator::generateList()
{
    m_internalList.clear();

    //searches in a range around the selected QPoint
    for(int i = -10 + m_point.x(); i < 10 + m_point.y(); i++)
        for(int j = -10 + m_point.y(); j < 10 + m_point.y(); j++)
        {
            PlanesCommonTools::Coordinate2D qp(i, j);
            //generates all planes intersecting the point
            PlaneIntersectingPointIterator pipi(qp);

            bool pointFound = false;

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
                m_internalList.push_back(qp);
                continue;
            }
        }
}
