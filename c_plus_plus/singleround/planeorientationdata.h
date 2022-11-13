#ifndef _PLANE_ORIENTATION_DATA_
#define _PLANE_ORIENTATION_DATA_

#include "plane.h"
#include "guesspoint.h"

struct PlaneOrientationData
{
    //the position of the plane
    Plane m_plane;

    //whether this orientation was discarded
    bool m_discarded;
    //points on this plane that were not tested
    //if m_discarded is false it means that all the
    //tested points were hits
    std::vector<PlanesCommonTools::Coordinate2D> m_pointsNotTested;

    //default constructor
    PlaneOrientationData();
    //another constructor
    PlaneOrientationData(const Plane& pl, bool isDiscarded);
    //copy constructor
    PlaneOrientationData(const PlaneOrientationData& pod);
    //equals operator
    void operator=(const PlaneOrientationData& pod);

    //update the info about this plane with another guess point
    //a guess point is a pair (position, guess result)
    void update(const GuessPoint& gp);
    //verifies if all the points in the current orientation were already checked
    bool areAllPointsChecked();
};

#endif