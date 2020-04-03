#ifndef PLANEGRID_H
#define PLANEGRID_H

#include "plane.h"
#include "guesspoint.h"
#include "coordinate2d.h"

/**Implements the logic of planes in a grid.
*Manages a list of plane positions and orientations.
*/
class PlaneGrid
{
private:
    //number of rows and columns
    int m_rowNo, m_colNo;
    //number of planes
    int m_planeNo;
    //whether the grid belongs to computer or to player
    bool m_isComputer;
    //list of plane objects for the grid
    std::vector<Plane> m_planeList;
    //list of all points on the planes
    std::vector<PlanesCommonTools::Coordinate2D> m_listPlanePoints;
    //whether planes overlap. is computed every time the plane points are computed again.
    bool m_PlanesOverlap = false;
    //whether a plane is outside of the grid
    bool m_PlaneOutsideGrid = false;

    ///for QML
    std::vector<int> m_listPlanePointsAnnotations;
    //the following annotations should exist
    //00000001 - belonging to plane 1
    //00000010 - head of plane 1
    //00000100 - belonging to plane 2
    //00001000 - head of plane 2
    //00010000 - belonging to plane 3
    //00100000 - head of plane 3

public:
    //constructor
    PlaneGrid(int row, int col, int planesNo, bool isComputer);
    //initializes the grid
    void initGrid();
    //searches a plane in the list of planes
    int searchPlane(const Plane& pl) const;
    //searches a plane for a given  plane head position
    int searchPlane(int row, int col) const;
    //adds a plane to the list of planes
    bool savePlane(const Plane& pl);
    //removes a plane from the list of planes
    bool removePlane(int idx, Plane &pl);
    //resets the plane grid
    void resetGrid();
    //returns whether a point is on a plane or not
    //additionaly it returns the position of the point on the plane
    bool isPointOnPlane(int row, int col, int& idx) const;
    /***
     * computes the list of plane points
     * @param[in] - sendSignal, whether to send signal that a new configuration was computed
     ***/
    bool computePlanePointsList(bool sendSignal);
    //returns the size of the plane list
    int getPlaneListSize() const;
    //returns a plane from the list of planes
    bool getPlane(int pos, Plane &pl) const;
    //returns the number of planes that we should draw
    int getPlaneNo() const { return m_planeNo; }
    //returns whether the grid belongs to a computer or not
    bool isComputer() const { return m_isComputer; }
    //gets the size of the grid
    int getRowNo() const { return m_rowNo; }
    int getColNo() const { return m_colNo; }
    //generates a random position on the grid
    PlanesCommonTools::Coordinate2D generateRandomGridPosition() const;
    //finds how good is a guess
    GuessPoint::Type getGuessResult(const PlanesCommonTools::Coordinate2D& qp) const;

    bool rotatePlane(int idx);
    bool movePlaneUpwards(int idx);
    bool movePlaneDownwards(int idx);
    bool movePlaneLeft(int idx);
    bool movePlaneRight(int idx);

    inline bool doPlanesOverlap() { return m_PlanesOverlap; }
    inline bool isPlaneOutsideGrid() { return m_PlaneOutsideGrid; }

    inline bool isPointInGrid(const PlanesCommonTools::Coordinate2D& qp) {
        if (qp.x() < 0 || qp.y() < 0)
            return false;
        if (qp.x() >= getColNo() || qp.y() >= getRowNo())
            return false;
        return true;
    }

///for integration with QML
    int getPlanesPointsCount() const { return static_cast<int>(m_listPlanePoints.size()); }
    PlanesCommonTools::Coordinate2D getPlanePoint(int idx) const { return m_listPlanePoints[idx]; }
    //retrieves additional information about a plane point
    //the plane idx, whether it is a plane head or not
    int getPlanePointAnnotation(int idx) const { return m_listPlanePointsAnnotations[idx]; }
    //transforms the annotation in a list of plane ids
    std::vector<int> decodeAnnotation(int annotation) const;

private:
    //generates a plane at a random position on the grid
    Plane generateRandomPlane() const;

    //generates a random plane orientation
    Plane::Orientation generateRandomPlaneOrientation() const;
    //randomly generates grid with planes
    bool initGridByAutomaticGeneration();

    //removes a given plane from the list of planes
    void removePlane(const Plane& pl);
    //returns whether a point is head of a plane or not
    bool isPointHead(int row, int col) const;
    //verifies if a plane position is valid within the grid
    bool isPlanePosValid(const Plane& pl) const;

    ///for QML
    //generates annotation for one point on a given plane
    //this is not the final annotation of the point
    //when it belongs to more planes the function is called
    //more times and the results are combined
    int generateAnnotation(int planeNo, bool isHead);
};

#endif // PLANEGRID_H
