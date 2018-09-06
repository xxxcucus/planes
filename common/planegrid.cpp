#include "planegrid.h"
#include "planeiterators.h"
#include "coordinate2d.h"
#include <QList>
#include <QDebug>
#include <cstdlib>

PlaneGrid::PlaneGrid(int row, int col, int planesNo, bool isComputer):
    m_rowNo(row),
    m_colNo(col),
    m_planeNo(planesNo),
    m_isComputer(isComputer)
{
    resetGrid();
}

//adds planes to the grid
void PlaneGrid::initGrid()
{
    resetGrid();

    initGridByAutomaticGeneration();
    if (!m_isComputer)
        emit initPlayerGrid();
    //compute list of plane points - needed for the guessing process
    computePlanePointsList(true);
}

//randomly generates grid with planes
bool PlaneGrid::initGridByAutomaticGeneration()
{
    int count = 0;
    QList<Plane> listPossiblePositions;
    listPossiblePositions.clear();

    //build a list of all possible positions
    //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
    for(int i = 0; i < m_rowNo; i++)
        for(int j = 0; j < m_colNo; j++)
            for(int k = 0;k < 4; k++)
            {
                Plane pl(i, j, (Plane::Orientation)k);
                listPossiblePositions.append(pl);
            }

    while(count < m_planeNo)
    {
        //generate a list iterator
        QMutableListIterator<Plane> i(listPossiblePositions);

        //elimintate all positions that are valid considering the already
        //created planes
        while(i.hasNext())
        {
            Plane pl = i.next();

            //if the plane is outside of the grid remove the position
            if(!isPlanePosValid(pl))
            {
                i.remove();
                continue;
            }
            //if the plane is already in the list remove the position
            //also save the plane in the list of planes
            if(!savePlane(pl))
            {
                i.remove();
                continue;
            }

            //compute all the points on planes and check for intersections
            if(!computePlanePointsList(false))
            {
                i.remove();
                removePlane(pl);
                continue;
            }
            else
                removePlane(pl);

        }

        //if no positions are left in the list return false
        if(listPossiblePositions.size() == 0)
            return false;

        //from the positions that are left in the list
        //choose a random one
        int pos = Plane::generateRandomNumber(listPossiblePositions.size());

        Plane pl = listPossiblePositions.at(pos);
        //save the selected plane
        if(savePlane(pl))
            count++;
    } //while
    return true;
}

//generate a plane at a random grid position
Plane PlaneGrid::generateRandomPlane() const
{
    PlanesCommonTools::Coordinate2D qp = generateRandomGridPosition();
    Plane::Orientation orient = generateRandomPlaneOrientation();
    return Plane(qp, orient);
}

//generates a random position on the grid
PlanesCommonTools::Coordinate2D PlaneGrid::generateRandomGridPosition() const
{

    int idx = Plane::generateRandomNumber(m_rowNo * m_colNo);

    return PlanesCommonTools::Coordinate2D(idx % m_rowNo,idx / m_rowNo);
}

//generates a random plane orientation
Plane::Orientation PlaneGrid::generateRandomPlaneOrientation() const
{
int idx = Plane::generateRandomNumber(4);
    switch(idx)
    {
    case 0: return Plane::NorthSouth;
    case 1: return Plane::SouthNorth;
    case 2: return Plane::EastWest;
    case 3: return Plane::WestEast;
    default: return Plane::NorthSouth;
    }
}

//let's the user generate his own planes
void PlaneGrid::initGridByUserInteraction() const
{
    emit initPlayerGrid();
}

//returns whether a point is head of a plane or not
bool PlaneGrid::isPointHead(int row, int col) const
{
    if(searchPlane(row, col)!=-1)
        return true;
    else return false;
}

//returns whether a point is on a plane or not
//and additionally the index position where the point occurs
//in the list of planes
bool PlaneGrid::isPointOnPlane(int row, int col, int& idx) const
{
    idx = m_listPlanePoints.indexOf(PlanesCommonTools::Coordinate2D(row, col));
    return (idx >= 0);
}

//computes all the points on a plane
//and returns false if planes intersect and true otherwise
//also detects if a plane lies outside of the grid
//also marks to which plane does the point belong and wether is a plane head or not
bool PlaneGrid::computePlanePointsList(bool sendSignal)
{
    m_listPlanePoints.clear();
    m_listPlanePointsAnnotations.clear();
    bool returnValue = true;

    m_PlaneOutsideGrid = false;
    for(int i = 0; i < m_planeList.size(); i++)
    {
        Plane pl = m_planeList.at(i);
        PlanePointIterator ppi(pl);
        bool isHead = true;

        while(ppi.hasNext())
        {
            PlanesCommonTools::Coordinate2D qp = ppi.next();
            if (!isPointInGrid(qp))
                m_PlaneOutsideGrid = true;
            ///compute the point's annotation
            int annotation = generateAnnotation(i, isHead);
            int idx = 0;
            if(!isPointOnPlane(qp.x(), qp.y(), idx)) {
                m_listPlanePoints.append(qp);
                m_listPlanePointsAnnotations.push_back(annotation);
            } else {
                returnValue = false;
                m_listPlanePointsAnnotations[idx] |= annotation;
            }
            isHead = false;
        }
    }

    m_PlanesOverlap = !returnValue;
    if (sendSignal)
        emit planesPointsChanged();
    return returnValue;
}

//searches a plane in the list of planes
int PlaneGrid::searchPlane(const Plane& pl) const
{
    return m_planeList.indexOf(pl);
}

//searches a plane with the head at a given position on the grid in the list of planes
int PlaneGrid::searchPlane(int row, int col) const
{
    for(int i = 0; i < m_planeList.size(); i++)
    {
        Plane plane = m_planeList.at(i);

        if((plane.row() == row) && (plane.col() == col))
            return i;
    }

    return -1;
}

//saves a plane
bool PlaneGrid::savePlane(const Plane& pl)
{
    //to check if plane is already in list
    if(searchPlane(pl) == -1)
    {
        //append to plane list
        m_planeList.append(pl);

        return true;
    }
    return false;
}

//removes the plane at a given position in the list of planes
bool PlaneGrid::removePlane(int idx, Plane &pl)
{
    if(idx < 0 || idx >= m_planeList.size())
        return false;

     pl = m_planeList.at(idx);
     //remove the plane from the list of planes
     m_planeList.removeAt(idx);
     return true;
}

//removes a plane from the grid
void PlaneGrid::removePlane(const Plane& pl)
{
    m_planeList.removeOne(pl);
}

//resets the plane grid
void PlaneGrid::resetGrid()
{
    m_planeList.clear();
    m_listPlanePointsAnnotations.clear();
    m_listPlanePoints.clear();
    emit planesPointsChanged();
    //m_guessPointList.clear();
}

//checks whether a plane is inside the grid
bool PlaneGrid::isPlanePosValid(const Plane& pl) const
{
    return pl.isPositionValid(m_rowNo, m_colNo);
}

//returns the size of the plane list
int PlaneGrid::getPlaneListSize() const
{
    return m_planeList.size();
}

//gets the plane at a given position in the list of planes
bool PlaneGrid::getPlane(int pos, Plane& pl) const
{
    if(pos < 0 || pos >= m_planeList.size())
        return false;

    pl = m_planeList.at(pos);
    return true;
}

bool PlaneGrid::rotatePlane(int idx)
{
    if (idx < 0 || idx >= m_planeList.size())
        return false;
    Plane& pl = m_planeList[idx];
    pl.rotate();
    ///@todo: don't know how this will work when the plane comes out of the grid
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneUpwards(int idx)
{
    if (idx < 0 || idx >= m_planeList.size())
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(0, -1, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneDownwards(int idx)
{
    if (idx < 0 || idx >= m_planeList.size())
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(0, 1, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneLeft(int idx)
{
    if (idx < 0 || idx >= m_planeList.size())
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(-1, 0, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneRight(int idx)
{
    if (idx < 0 || idx >= m_planeList.size())
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(1, 0, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

//for a given Coordinate2D checks to what type of point it corresponds in the grid
GuessPoint::Type PlaneGrid::getGuessResult(const PlanesCommonTools::Coordinate2D& qp) const
{
    if(isPointHead(qp.x(), qp.y()))
        return GuessPoint::Dead;

    int idx = 0;
    if(isPointOnPlane(qp.x(), qp.y(), idx))
        return GuessPoint::Hit;

    return GuessPoint::Miss;
}

int PlaneGrid::generateAnnotation(int planeNo, bool isHead) {
    int annotation = 1;
    int bitsShifted = 2 * planeNo;
    if (isHead)
        bitsShifted++;
    annotation = annotation << bitsShifted;
    return annotation;
}

std::vector<int> PlaneGrid::decodeAnnotation(int annotation) const {
    std::vector<int> retVal;
    for (int i = 0; i < m_planeNo; ++i) {
        int mask = 0x3 << (2 * i);
        int mask1 = 0x1 << (2 * i);
        int mask2 = 0x2 << (2 * i);
        /*if (mask & annotation)
            retVal.push_back(i);*/
        if (mask1 & annotation)
            retVal.push_back(i);
        if (mask2 & annotation)
            retVal.push_back(-i - 1);
    }
    return retVal;
}
