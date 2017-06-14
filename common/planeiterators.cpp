#include "planeiterators.h"

//constructor
PlanePointIterator::PlanePointIterator(const Plane &pl):
    MyIterator::ListIterator<QPoint>(),
    m_plane(pl)
{
    generateList();
}

//the function that generates the list of points
void PlanePointIterator::generateList()
{

    const QPoint pointsNorthSouth[] = {QPoint(0,0), QPoint(0,1), QPoint(-1,1), QPoint(1,1), QPoint(-2,1), QPoint(2,1), QPoint(0,2),
                                   QPoint(0,3), QPoint(-1,3), QPoint(1,3)};

    const QPoint pointsSouthNorth[] = {QPoint(0,0), QPoint(0,-1), QPoint(-1,-1), QPoint(1,-1), QPoint(-2,-1), QPoint(2,-1), QPoint(0,-2),
                                   QPoint(0,-3), QPoint(-1,-3), QPoint(1,-3)};

    const QPoint pointsEastWest[] = {QPoint(0,0), QPoint(1,0), QPoint(1,-1), QPoint(1,1), QPoint(1,-2), QPoint(1,2), QPoint(2,0),
                                 QPoint(3,0), QPoint(3,-1), QPoint(3,1)};

    const QPoint pointsWestEast[] = {QPoint(0,0), QPoint(-1,0), QPoint(-1,-1), QPoint(-1,1), QPoint(-1,-2), QPoint(-1,2), QPoint(-2,0),
                                 QPoint(-3,0), QPoint(-3,1), QPoint(-3,-1)};

    const int size = 10;
    for(int i=0;i<size;i++)
    {
        switch(m_plane.orientation())
        {
            case Plane::NorthSouth:
                m_internalList << pointsNorthSouth[i]+m_plane.head();
                break;
            case Plane::SouthNorth:
                m_internalList << pointsSouthNorth[i]+m_plane.head();
                break;
            case Plane::WestEast:
                m_internalList << pointsWestEast[i]+m_plane.head();
                break;
            case Plane::EastWest:
                m_internalList << pointsEastWest[i]+m_plane.head();
                break;
            default:
                ;
        }
    }
}

//constructor for the iterator giving all the planes
//passing through the point (0,0)
PlaneIntersectingPointIterator::PlaneIntersectingPointIterator(const QPoint &qp):
    MyIterator::ListIterator<Plane>(),
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
    for(int i=-5+m_point.x();i<6+m_point.x();i++)
        for(int j=-5+m_point.y();j<6+m_point.y();j++)
            for(int k=0;k<4;k++)
            {
                Plane pl(i,j,(Plane::Orientation)k);
                m_internalList.append(pl);
            }

    QMutableListIterator <Plane> i(m_internalList);

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

PointInfluenceIterator::PointInfluenceIterator(const QPoint &qp):
    MyIterator::ListIterator<QPoint>(),
    m_point(qp)
{
    generateList();
}

void PointInfluenceIterator::generateList()
{
    m_internalList.clear();

    //searches in a range around the selected QPoint
    for(int i=-10+m_point.x();i<10+m_point.y();i++)
        for(int j=-10+m_point.y();j<10+m_point.y();j++)
        {
            QPoint qp(i,j);
            //generates all planes intersecting the point
            PlaneIntersectingPointIterator pipi(qp);

            bool pointFound = false;

            //check to see if any of these planes correspond to the initial point
            while(pipi.hasNext())
            {
                Plane pl= pipi.next();
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

