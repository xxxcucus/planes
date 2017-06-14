#ifndef PLANEITERATORS_H
#define PLANEITERATORS_H

#include "plane.h"

//iterates over the points that make a plane
class PlanePointIterator : public ListIterator<QPoint>
{
    Plane m_plane;
public:
    PlanePointIterator(const Plane &pl);

private:
    void generateList();
};

//lists the relatives positions of all planes that pass through the point (0,0)
class PlaneIntersectingPointIterator: public ListIterator<Plane>
{
    QPoint m_point;

public:
    //constructor taking a QPoint
    PlaneIntersectingPointIterator(const QPoint &qp=QPoint(0,0));

private:
    //generates list of plane indexes that pass through (0,0)
    void generateList();
};

//lists the points that can influence the value of a point
class PointInfluenceIterator: ListIterator<QPoint>
{
    QPoint m_point;

public:
    //constructor
    PointInfluenceIterator(const QPoint &qp=QPoint(0,0));

private:
    //generates the list points influencing the point (0,0)
    void generateList();
};

#endif // PLANEITERATORS_H
