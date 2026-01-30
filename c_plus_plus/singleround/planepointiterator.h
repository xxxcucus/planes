#ifndef __PLANE_POINT_ITERATOR__
#define __PLANE_POINT_ITERATOR__

#include "plane.h"
#include "coordinate2d.h"
#include "vectoriterator.h"

//iterates over the points that make a plane
class PlanePointIterator : public PlanesCommonTools::VectorIterator<PlanesCommonTools::Coordinate2D>
{
    Plane m_plane;
public:
    PlanePointIterator(const Plane& pl);


private:
    void generateList();
};



#endif // __PLANE_POINT_ITERATOR__
