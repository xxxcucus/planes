
#include "planepointiterator.h"
#include "planeintersectingpointiterator.h"
#include "coordinate2d.h"
#include <algorithm>


//constructor for the iterator giving all the planes
//passing through the point (0,0)
PlaneIntersectingPointIterator::PlaneIntersectingPointIterator(const PlanesCommonTools::Coordinate2D& qp):
    PlanesCommonTools::VectorIterator<Plane>(),
    m_point(qp)
{
    //generates the list of planes
    generateListOptimized();
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
}


void PlaneIntersectingPointIterator::generateListOptimized() {


    m_internalList.clear();

    for (int i = 0; i < generatorCount; i++) {
        Plane pl = generatorList[i];
        Plane transPl = pl + m_point;
        m_internalList.push_back(transPl);
    }
}