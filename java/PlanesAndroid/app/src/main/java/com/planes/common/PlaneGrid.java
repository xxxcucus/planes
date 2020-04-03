package com.planes.common;

import java.util.Iterator;
import java.util.Vector;

/**Implements the logic of planes in a grid.
 *Manages a list of plane positions and orientations.
 */
public class PlaneGrid {

    //constructor
    public PlaneGrid(int row, int col, int planesNo, boolean isComputer) {
        m_rowNo = row;
        m_colNo = col;
        m_planeNo = planesNo;
        m_isComputer = isComputer;

        initGrid();
    }
    //initializes the grid
    public void initGrid() {
        resetGrid();
        initGridByAutomaticGeneration();
        //compute list of plane points - needed for the guessing process
        computePlanePointsList(true);
    }
    //searches a plane in the list of planes
    public int searchPlane(final Plane pl) {
        return m_planeList.indexOf(pl);
    }
    //searches a plane for a given  plane head position
    public int searchPlane(int row, int col) {
        for(int i = 0; i < m_planeList.size(); i++)
        {
            Plane plane = m_planeList.get(i);

            if((plane.row() == row) && (plane.col() == col))
                return i;
        }

        return -1;
    }
    //adds a plane to the list of planes
    public boolean savePlane(final Plane pl) {
        //to check if plane is already in list
        if(searchPlane(pl) == -1)
        {
            //append to plane list
            m_planeList.add((Plane)pl.clone());

            return true;
        }
        return false;
    }
    //removes a plane from the list of planes
    public boolean removePlane(int idx, Plane pl) {
        if(idx < 0 || idx >= m_planeList.size())
            return false;

        pl = (Plane)m_planeList.get(idx).clone();
        //remove the plane from the list of planes
        m_planeList.remove(idx);
        return true;
    }
    //resets the plane grid
    public void resetGrid() {
        m_planeList.clear();
        m_listPlanePointsAnnotations.clear();
        m_listPlanePoints.clear();
    }
    //returns whether a point is on a plane or not
    //additionaly it returns the position of the point on the plane
    public boolean isPointOnPlane(int row, int col, Integer idx) {
        idx = m_listPlanePoints.indexOf(new Coordinate2D(row, col));
        if (idx < 0)
            return false;
        return true;
    }
    /***
     * computes the list of plane points
     * @param[in] - sendSignal, whether to send signal that a new configuration was computed
     ***/

    //computes all the points on a plane
    //and returns false if planes intersect and true otherwise
    //also detects if a plane lies outside of the grid
    //also marks to which plane does the point belong and wether is a plane head or not
    public boolean computePlanePointsList(boolean sendSignal) {
        m_listPlanePoints.clear();
        m_listPlanePointsAnnotations.clear();
        boolean returnValue = true;

        m_PlaneOutsideGrid = false;
        for(int i = 0; i < m_planeList.size(); i++)
        {
            Plane pl = m_planeList.get(i);
            PlanePointIterator ppi = new PlanePointIterator(pl);
            boolean isHead = true;

            while (ppi.hasNext())
            {
                Coordinate2D qp = ppi.next();
                if (!isPointInGrid(qp))
                    m_PlaneOutsideGrid = true;
                ///compute the point's annotation
                int annotation = generateAnnotation(i, isHead);
                int idx = 0;
                if(!isPointOnPlane(qp.x(), qp.y(), idx)) {
                    m_listPlanePoints.add((Coordinate2D)qp.clone());
                    //TODO: clone an integer ?

                    m_listPlanePointsAnnotations.add(annotation);
                } else {
                    returnValue = false;
                    m_listPlanePointsAnnotations.set(idx, m_listPlanePointsAnnotations.get(idx) | annotation);
                }
                isHead = false;
            }
        }

        m_PlanesOverlap = !returnValue;
        return returnValue;
    }
    //returns the size of the plane list
    public int getPlaneListSize() {
        return m_planeList.size();
    }
    //returns a plane from the list of planes
    public boolean getPlane(int pos, Plane pl) {
        if(pos < 0 || pos >= m_planeList.size())
            return false;

        pl = m_planeList.get(pos);
        return true;
    }
    //returns the number of planes that we should draw
    public int getPlaneNo()  {
        return m_planeNo;
    }
    //returns whether the grid belongs to a computer or not
    public boolean isComputer() {
        return m_isComputer;
    }
    //gets the size of the grid
    public int getRowNo() {
        return m_rowNo;
    }
    public int getColNo()  {
        return m_colNo;
    }
    //generates a random position on the grid
    public Coordinate2D generateRandomGridPosition() {
        int idx = Plane.generateRandomNumber(m_rowNo * m_colNo);
        return new Coordinate2D(idx % m_rowNo, idx / m_rowNo);
    }
    //finds how good is a guess
    public Type getGuessResult(final Coordinate2D qp) {
        if(isPointHead(qp.x(), qp.y()))
            return Type.Dead;

        int idx = 0;
        if(isPointOnPlane(qp.x(), qp.y(), idx))
            return Type.Hit;

        return Type.Miss;
    }

    public boolean rotatePlane(int idx) {
        if (idx < 0 || idx >= m_planeList.size())
        return false;
        Plane pl = m_planeList.get(idx);
        pl.rotate();
        ///@todo: don't know how this will work when the plane comes out of the grid
        computePlanePointsList(true);
        return true;
    }

    public boolean movePlaneUpwards(int idx) {
        if (idx < 0 || idx >= m_planeList.size())
        return false;
        Plane pl = m_planeList.get(idx);
        pl.translateWhenHeadPosValid(0, -1, m_rowNo, m_colNo);
        computePlanePointsList(true);
        return true;
    }
    public boolean movePlaneDownwards(int idx) {
        if (idx < 0 || idx >= m_planeList.size())
        return false;
        Plane pl = m_planeList.get(idx);
        pl.translateWhenHeadPosValid(0, 1, m_rowNo, m_colNo);
        computePlanePointsList(true);
        return true;
    }
    public boolean movePlaneLeft(int idx) {
        if (idx < 0 || idx >= m_planeList.size())
        return false;
        Plane pl = m_planeList.get(idx);
        pl.translateWhenHeadPosValid(-1, 0, m_rowNo, m_colNo);
        computePlanePointsList(true);
        return true;
    }
    public boolean movePlaneRight(int idx) {
        if (idx < 0 || idx >= m_planeList.size())
        return false;
        Plane pl = m_planeList.get(idx);
        pl.translateWhenHeadPosValid(1, 0, m_rowNo, m_colNo);
        computePlanePointsList(true);
        return true;
    }

    public boolean doPlanesOverlap() {
        return m_PlanesOverlap;
    }
    public boolean isPlaneOutsideGrid() {
        return m_PlaneOutsideGrid;
    }

    public boolean isPointInGrid(final Coordinate2D qp) {
        if (qp.x() < 0 || qp.y() < 0)
            return false;
        if (qp.x() >= getColNo() || qp.y() >= getRowNo())
            return false;
        return true;
    }

    ///for integration with QML
    public int getPlanesPointsCount() {
        return (int)(m_listPlanePoints.size());
    }
    public Coordinate2D getPlanePoint(int idx) {
        return m_listPlanePoints.get(idx);
    }
    //retrieves additional information about a plane point
    //the plane idx, whether it is a plane head or not
    public int getPlanePointAnnotation(int idx) {
        return m_listPlanePointsAnnotations.get(idx);
    }
    //transforms the annotation in a list of plane ids
    public Vector<Integer> decodeAnnotation(int annotation) {
        Vector<Integer> retVal = new Vector<Integer>();
        for (int i = 0; i < m_planeNo; ++i) {
            //int mask = 0x3 << (2 * i);
            int mask1 = 0x1 << (2 * i);
            int mask2 = 0x2 << (2 * i);
        /*if (mask & annotation)
            retVal.push_back(i);*/
            if ((mask1 & annotation) > 0)
                retVal.add(i);
            if ((mask2 & annotation) > 0)
                retVal.add(-i - 1);
        }
        return retVal;
    }


    //generates a plane at a random position on the grid
    private Plane generateRandomPlane() {
        Coordinate2D qp = generateRandomGridPosition();
        Orientation orient = generateRandomPlaneOrientation();
        return new Plane(qp, orient);
    }

    //generates a random plane orientation
    private Orientation generateRandomPlaneOrientation() {
        int idx = Plane.generateRandomNumber(4);
        switch(idx)
        {
            case 0: return Orientation.NorthSouth;
            case 1: return Orientation.SouthNorth;
            case 2: return Orientation.EastWest;
            case 3: return Orientation.WestEast;
            default: return Orientation.NorthSouth;
        }

    }
    //randomly generates grid with planes
    private boolean initGridByAutomaticGeneration() {
        int count = 0;
        Vector<Plane>  listPossiblePositions = new Vector<Plane>();

        //build a list of all possible positions
        //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
        for(int i = 0; i < m_rowNo; i++)
            for(int j = 0; j < m_colNo; j++)
                for(int k = 0; k < 4; k++)
                {
                    Plane pl = new Plane(i, j, Orientation.values()[k]);
                    listPossiblePositions.add(pl);
                }

        while(count < m_planeNo)
        {
            //generate a list iterator
            Iterator<Plane> it = listPossiblePositions.iterator();
            //elimintate all positions that are valid considering the already
            //created planes
            while (it.hasNext())
            {
                Plane pl = it.next();
                //if the plane is outside of the grid remove the position
                if (!isPlanePosValid(pl))
                {
                    it.remove();
                    continue;
                }
                //if the plane is already in the list remove the position
                //also save the plane in the list of planes
                if (!savePlane(pl))
                {
                    it.remove();
                    continue;
                }

                //compute all the points on planes and check for intersections
                if(!computePlanePointsList(false))
                {
                    removePlane(pl);
                    it.remove();
                    continue;
                }
                else {
                    removePlane(pl);
                }
            }

            //if no positions are left in the list return false
            if(listPossiblePositions.isEmpty())
                return false;

            //from the positions that are left in the list
            //choose a random one
            int pos = Plane.generateRandomNumber(listPossiblePositions.size());

            Plane pl = listPossiblePositions.get(pos);
            //save the selected plane
            if (savePlane(pl))
                count++;
        } //while

        return true;
    }

    //removes a given plane from the list of planes
    private void removePlane(final Plane pl) {
        m_planeList.remove(pl);
    }
    //returns whether a point is head of a plane or not
    private boolean isPointHead(int row, int col) {
        if(searchPlane(row, col) != -1)
            return true;
        else return false;
    }
    //verifies if a plane position is valid within the grid
    private boolean isPlanePosValid(final Plane pl) {
        return pl.isPositionValid(m_rowNo, m_colNo);
    }

    ///for QML
    //generates annotation for one point on a given plane
    //this is not the final annotation of the point
    //when it belongs to more planes the function is called
    //more times and the results are combined
    private int generateAnnotation(int planeNo, boolean isHead) {
        int annotation = 1;
        int bitsShifted = 2 * planeNo;
        if (isHead)
            bitsShifted++;
        annotation = annotation << bitsShifted;
        return annotation;
    }

    //number of rows and columns
    private int m_rowNo, m_colNo;
    //number of planes
    private int m_planeNo;
    //whether the grid belongs to computer or to player
    private boolean m_isComputer;
    //list of plane objects for the grid
    private Vector<Plane> m_planeList;
    //list of all points on the planes
    private Vector<Coordinate2D> m_listPlanePoints;
    //whether planes overlap. is computed every time the plane points are computed again.
    private boolean m_PlanesOverlap = false;
    //whether a plane is outside of the grid
    private boolean m_PlaneOutsideGrid = false;

    ///for QML
    private Vector<Integer> m_listPlanePointsAnnotations;
    //the following annotations should exist
    //00000001 - belonging to plane 1
    //00000010 - head of plane 1
    //00000100 - belonging to plane 2
    //00001000 - head of plane 2
    //00010000 - belonging to plane 3
    //00100000 - head of plane 3
}
