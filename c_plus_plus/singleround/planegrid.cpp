#include <cstdlib>
#include <algorithm>
#include "planegrid.h"
#include "planeiterators.h"
#include "coordinate2d.h"

PlaneGrid::PlaneGrid(int row, int col, int planesNo, bool isComputer):
    m_rowNo(row),
    m_colNo(col),
    m_planeNo(planesNo),
    m_isComputer(isComputer)
{
    //resetGrid();
	initGrid();
}

//adds planes to the grid
void PlaneGrid::initGrid()
{
    resetGrid();

    initGridByAutomaticGeneration();
    //commented because of interfacing with Java
	/*if (!m_isComputer)
        emit initPlayerGrid();*/
    //compute list of plane points - needed for the guessing process
    computePlanePointsList(true);
}

//randomly generates grid with planes
bool PlaneGrid::initGridByAutomaticGeneration()
{
    int count = 0;
    std::vector<Plane> listPossiblePositions;
    listPossiblePositions.clear();

    //build a list of all possible positions
    //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
    for(int i = 0; i < m_rowNo; i++)
        for(int j = 0; j < m_colNo; j++)
            for(int k = 0; k < 4; k++)
            {
                Plane pl(i, j, (Plane::Orientation)k);
                listPossiblePositions.push_back(pl);
            }

    while(count < m_planeNo)
    {
        //generate a list iterator
        auto it = listPossiblePositions.begin();
        //elimintate all positions that are valid considering the already
        //created planes
        while (it != listPossiblePositions.end())
        {
            //if the plane is outside of the grid remove the position
            if (!isPlanePosValid(*it))
            {
                it = listPossiblePositions.erase(it);
                continue;
            }
            //if the plane is already in the list remove the position
            //also save the plane in the list of planes
            if (!savePlane(*it))
            {
                it = listPossiblePositions.erase(it);
                continue;
            }

            //compute all the points on planes and check for intersections
            if(!computePlanePointsList(false))
            {
                removePlane(*it);
                it = listPossiblePositions.erase(it);
                continue;
            }
            else {
                removePlane(*it);
            }
            ++it;
        }

        //if no positions are left in the list return false
        if(listPossiblePositions.empty())
            return false;

        //from the positions that are left in the list
        //choose a random one
        int pos = Plane::generateRandomNumber(static_cast<int>(listPossiblePositions.size()));

        Plane pl = listPossiblePositions[pos];
        //save the selected plane
        if (savePlane(pl))
            count++;
    } //while
    
	return true;
}

bool PlaneGrid::initGridByUser(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient) {
    if (m_planeNo != 3)
        return false;
    
    Plane pl1(plane1_x, plane1_y, plane1_orient);
    Plane pl2(plane2_x, plane2_y, plane2_orient);
    Plane pl3(plane3_x, plane3_y, plane3_orient);
    
    resetGrid();
    
    if (!savePlane(pl1)) {
        printf("Fail 1");
        return false;
    }
    if (!savePlane(pl2)) {
        printf("Fail 2");
        return false;
    }
    if (!savePlane(pl3)) {
        printf("Fail 3");
        return false;
    }
    
    bool retval = computePlanePointsList(false);
    
    if (!retval) {
        printf("Fail 4");
    }
    
    return retval;
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

    return PlanesCommonTools::Coordinate2D(idx % m_rowNo, idx / m_rowNo);
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

//returns whether a point is head of a plane or not
bool PlaneGrid::isPointHead(int row, int col) const
{
    if(searchPlane(row, col) != -1)
        return true;
    else return false;
}

//returns whether a point is on a plane or not
//and additionally the index position where the point occurs
//in the list of planes
bool PlaneGrid::isPointOnPlane(int row, int col, int& idx) const
{
    auto it = std::find(m_listPlanePoints.begin(), m_listPlanePoints.end(), PlanesCommonTools::Coordinate2D(row, col));
    if (it == m_listPlanePoints.end()) {
        idx = -1;
        return false;
    }
    idx = int(std::distance(m_listPlanePoints.begin(), it));
    return true;
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
    for(unsigned int i = 0; i < m_planeList.size(); i++)
    {
        Plane pl = m_planeList.at(i);
        PlanePointIterator ppi(pl);
        bool isHead = true;

        while (ppi.hasNext())
        {
            PlanesCommonTools::Coordinate2D qp = ppi.next();
            if (!isPointInGrid(qp))
                m_PlaneOutsideGrid = true;
            ///compute the point's annotation
            int annotation = generateAnnotation(i, isHead);
            int idx = 0;
            if(!isPointOnPlane(qp.x(), qp.y(), idx)) {
                m_listPlanePoints.push_back(qp);
                m_listPlanePointsAnnotations.push_back(annotation);
            } else {
                returnValue = false;
                m_listPlanePointsAnnotations[idx] |= annotation;
            }
            isHead = false;
        }
    }

    m_PlanesOverlap = !returnValue;
    return returnValue;
}

//searches a plane in the list of planes
int PlaneGrid::searchPlane(const Plane& pl) const
{
    auto it = std::find(m_planeList.begin(), m_planeList.end(), pl);
    if (it == m_planeList.end())
        return -1;
    else
        return int(std::distance(m_planeList.begin(), it));
}

//searches a plane with the head at a given position on the grid in the list of planes
int PlaneGrid::searchPlane(int row, int col) const
{
    for(int i = 0; i < static_cast<int>(m_planeList.size()); i++)
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
        m_planeList.push_back(pl);

        return true;
    }
    return false;
}

//removes the plane at a given position in the list of planes
bool PlaneGrid::removePlane(int idx, Plane &pl)
{
    if(idx < 0 || idx >= static_cast<int>(m_planeList.size()))
        return false;

     pl = m_planeList.at(idx);
     //remove the plane from the list of planes
     auto it = m_planeList.begin() + idx;
     m_planeList.erase(it);
     return true;
}

//removes a plane from the grid
void PlaneGrid::removePlane(const Plane& pl)
{
    auto it = std::find(m_planeList.begin(), m_planeList.end(), pl);
    if (it != m_planeList.end())
        m_planeList.erase(it);
}

//resets the plane grid
void PlaneGrid::resetGrid()
{
    m_planeList.clear();
    m_listPlanePointsAnnotations.clear();
    m_listPlanePoints.clear();
	m_GuessList.clear();
}

//checks whether a plane is inside the grid
bool PlaneGrid::isPlanePosValid(const Plane& pl) const
{
    return pl.isPositionValid(m_rowNo, m_colNo);
}

//returns the size of the plane list
int PlaneGrid::getPlaneListSize() const
{
    return static_cast<int>(m_planeList.size());
}

//gets the plane at a given position in the list of planes
bool PlaneGrid::getPlane(int pos, Plane& pl) const
{
    if(pos < 0 || pos >= static_cast<int>(m_planeList.size()))
        return false;

    pl = m_planeList.at(pos);
    return true;
}

//gets the plane at a given position in the list of planes
bool PlaneGrid::getPlanePoints(int pos, std::vector<PlanesCommonTools::Coordinate2D>& list) const
{
	if (pos < 0 || pos >= static_cast<int>(m_planeList.size()))
		return false;

	Plane pl = m_planeList.at(pos);
	list = pl.getPlanePoints();
	return true;
}

bool PlaneGrid::rotatePlane(int idx)
{
    if (idx < 0 || idx >= static_cast<int>(m_planeList.size()))
        return false;
    Plane& pl = m_planeList[idx];
    pl.rotate();
    ///@todo: don't know how this will work when the plane comes out of the grid
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneUpwards(int idx)
{
    if (idx < 0 || idx >= static_cast<int>(m_planeList.size()))
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(0, -1, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneDownwards(int idx)
{
    if (idx < 0 || idx >= static_cast<int>(m_planeList.size()))
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(0, 1, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneLeft(int idx)
{
    if (idx < 0 || idx >= static_cast<int>(m_planeList.size()))
        return false;
    Plane& pl = m_planeList[idx];
    pl.translateWhenHeadPosValid(-1, 0, m_rowNo, m_colNo);
    computePlanePointsList(true);
    return true;
}

bool PlaneGrid::movePlaneRight(int idx)
{
    if (idx < 0 || idx >= static_cast<int>(m_planeList.size()))
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
        //int mask = 0x3 << (2 * i);
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
