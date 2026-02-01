#ifndef __POINT_INFLUENCE_ITERATOR__
#define __POINT_INFLUENCE_ITERATOR__

#include "plane.h"
#include "coordinate2d.h"
#include "vectoriterator.h"

//TODO: not used - to remove
//lists the points that can influence the value of a point
class PointInfluenceIterator: PlanesCommonTools::VectorIterator<PlanesCommonTools::Coordinate2D>
{
    PlanesCommonTools::Coordinate2D m_point;

public:
    //constructor
    PointInfluenceIterator(const PlanesCommonTools::Coordinate2D& qp = PlanesCommonTools::Coordinate2D(0,0));

private:
    //generates the list points influencing the point (0,0)
    void generateList();
};

#endif // __POINT_INFLUENCE_ITERATOR__
