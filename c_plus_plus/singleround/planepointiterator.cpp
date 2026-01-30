#include "planepointiterator.h"
#include "coordinate2d.h"
#include <algorithm>

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
    const int size = 10;
    for(int i = 0; i < size; ++i)
    {
        switch(m_plane.orientation())
        {
            case Plane::NorthSouth:
                m_internalList.push_back(Plane::pointsNorthSouth[i] + m_plane.head());
                break;
            case Plane::SouthNorth:
                m_internalList.push_back(Plane::pointsSouthNorth[i] + m_plane.head());
                break;
            case Plane::WestEast:
                m_internalList.push_back(Plane::pointsWestEast[i] + m_plane.head());
                break;
            case Plane::EastWest:
                m_internalList.push_back(Plane::pointsEastWest[i] + m_plane.head());
                break;
            default:
                ;
        }
    }
}
