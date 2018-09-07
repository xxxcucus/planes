#ifndef PLANEITERATORS_H
#define PLANEITERATORS_H

#include "plane.h"
#include "coordinate2d.h"

//iterates over the points that make a plane
class PlanePointIterator : public VectorIterator<PlanesCommonTools::Coordinate2D>
{
    Plane m_plane;
public:
    PlanePointIterator(const Plane& pl);

private:
    void generateList();
};

//lists the relatives positions of all planes that pass through the point (0,0)
class PlaneIntersectingPointIterator: public VectorIterator<Plane>
{
    PlanesCommonTools::Coordinate2D m_point;

public:
    //constructor taking a QPoint
    PlaneIntersectingPointIterator(const PlanesCommonTools::Coordinate2D& qp = PlanesCommonTools::Coordinate2D(0,0));

private:
    //generates list of plane indexes that pass through (0,0)
    void generateList();
};

//lists the points that can influence the value of a point
class PointInfluenceIterator: VectorIterator<PlanesCommonTools::Coordinate2D>
{
    PlanesCommonTools::Coordinate2D m_point;

public:
    //constructor
    PointInfluenceIterator(const PlanesCommonTools::Coordinate2D& qp = PlanesCommonTools::Coordinate2D(0,0));

private:
    //generates the list points influencing the point (0,0)
    void generateList();
};

#endif // PLANEITERATORS_H
