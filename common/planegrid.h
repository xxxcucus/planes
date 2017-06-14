#ifndef PLANEGRID_H
#define PLANEGRID_H

#include "plane.h"
#include "guesspoint.h"
#include <QList>
#include <QPoint>
#include <QObject>

/**Implements the logic of planes in a grid.
*Manages a list of plane positions and orientations.
*/
class PlaneGrid: public QObject
{
    Q_OBJECT

    //number of rows and columns
    int m_rowNo, m_colNo;
    //number of planes
    int m_planeNo;
    //whether the grid belongs to a user or to a player
    bool m_isComputer;
    //list of plane objects for the grid
    QList <Plane> m_planeList;
    //list of all points on the planes
    QList <QPoint> m_listPlanePoints;
    //whether planes overlap. is computed every time the plane points are computed again.
    bool m_PlanesOverlap = false;
    //whether a plane is outside of the grid
    bool m_PlaneOutsideGrid = false;

public:
    //constructor
    PlaneGrid(int row, int col, int plane, bool isComputer);
    //initializes the grid
    void initGrid();
    //searches a plane in the list of planes
    int searchPlane(Plane pl) const;
    //searches a plane for a given  plane head position
    int searchPlane(int row, int col) const;
    //adds a plane to the list of planes
    bool savePlane(Plane pl);
    //removes a plane from the list of planes
    bool removePlane(int idx, Plane &pl);
    //resets the plane grid
    void resetGrid();
    //returns whether a point is on a plane or not
    bool isPointOnPlane(int row, int col) const;
    //computes the list of plane points
    bool computePlanePointsList();
    //returns the size of the plane list
    int getPlaneListSize() const;
    //returns a plane from the list of planes
    bool getPlane(int pos, Plane &pl) const;
    //returns the number of planes that we should draw
    int getPlaneNo() const {return m_planeNo; }
    //returns whether the grid belongs to a computer or not
    bool isComputer() const {return m_isComputer; }
    //gets the size of the grid
    int getRowNo() const {return m_rowNo; }
    int getColNo() const {return m_colNo; }
    //generates a random position on the grid
    QPoint generateRandomGridPosition() const;
    //finds how good is a guess
    GuessPoint::Type getGuessResult(QPoint qp) const;

    bool rotatePlane(int idx);
    bool movePlaneUpwards(int idx);
    bool movePlaneDownwards(int idx);
    bool movePlaneLeft(int idx);
    bool movePlaneRight(int idx);

    inline bool doPlanesOverlap() { return m_PlanesOverlap; }
    inline bool isPlaneOutsideGrid() { return m_PlaneOutsideGrid; }

    inline bool isPointInGrid(const QPoint& qp) {
        if (qp.x() < 0 || qp.y() < 0)
            return false;
        if (qp.x() >= getColNo() || qp.y() >= getRowNo())
            return false;
        return true;
    }

private:
    //generates a plane at a random position on the grid
    Plane generateRandomPlane() const;

    //generates a random plane orientation
    Plane::Orientation generateRandomPlaneOrientation() const;
    //randomly generates grid with planes
    bool initGridByAutomaticGeneration();
    //let's the user generate his own planes
    void initGridByUserInteraction() const;

    //removes a given plane from the list of planes
    void removePlane(Plane pl);
    //returns whether a point is head of a plane or not
    bool isPointHead(int row, int col) const;
    //verifies if a plane position is valid within the grid
    bool isPlanePosValid(Plane pl) const;

signals:
    void initPlayerGrid() const;  //emitted to notify the start of the user editing the plane lists

};

#endif // PLANEGRID_H
