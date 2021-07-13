package com.planes.common

import androidx.core.util.Pair
import com.planes.common.Plane.PlaneStatic.generateRandomNumber
import java.util.*

/**Implements the logic of planes in a grid.
 * Manages a list of plane positions and orientations.
 */
open class PlaneGrid(//gets the size of the grid
        //number of rows and columns
        var rowNo: Int, var colNo: Int, //returns the number of planes that we should draw
        //number of planes
        var planeNo: Int, //returns whether the grid belongs to a computer or not
        //whether the grid belongs to computer or to player
        var isComputer: Boolean) {


    //list of plane objects for the grid
    protected var m_planeList: Vector<Plane>

    //list of all points on the planes
    protected var m_listPlanePoints: Vector<Coordinate2D>

    //whether planes overlap. is computed every time the plane points are computed again.
    protected var m_PlanesOverlap = false

    //whether a plane is outside of the grid
    var isPlaneOutsideGrid = false

    ///for QML
    protected var m_listPlanePointsAnnotations: Vector<Int>

    //the following annotations should exist
    //00000001 - belonging to plane 1
    //00000010 - head of plane 1
    //00000100 - belonging to plane 2
    //00001000 - head of plane 2
    //00010000 - belonging to plane 3
    //00100000 - head of plane 3
    var guesses: Vector<GuessPoint>

    //constructor
    init {
        m_planeList = Vector()
        m_listPlanePoints = Vector()
        m_listPlanePointsAnnotations = Vector()
        guesses = Vector()
        initGrid()
    }

    //initializes the grid
    open fun initGrid() {
        resetGrid()
        initGridByAutomaticGeneration()
        //compute list of plane points - needed for the guessing process
        computePlanePointsList()
    }

    //searches a plane in the list of planes
    fun searchPlane(pl: Plane): Int {
        return m_planeList.indexOf(pl)
    }

    //searches a plane for a given  plane head position
    fun searchPlane(row: Int, col: Int): Int {
        for (i in m_planeList.indices) {
            val plane = m_planeList[i]
            if (plane.row() == row && plane.col() == col) return i
        }
        return -1
    }

    //adds a plane to the list of planes
    fun savePlane(pl: Plane): Boolean {
        //to check if plane is already in list
        if (searchPlane(pl) == -1) {
            //append to plane list
            m_planeList.add(pl.clone() as Plane)
            return true
        }
        return false
    }

    //removes a plane from the list of planes
    fun removePlane(idx: Int): Pair<Boolean, Plane> {
        if (idx < 0 || idx >= m_planeList.size) return Pair.create(false, Plane(0, 0, Orientation.NorthSouth))
        val pl = m_planeList[idx].clone() as Plane
        //remove the plane from the list of planes
        m_planeList.removeAt(idx)
        return Pair.create(true, pl)
    }

    //resets the plane grid
    fun resetGrid() {
        m_planeList.clear()
        m_listPlanePointsAnnotations.clear()
        m_listPlanePoints.clear()
        guesses.clear()
    }

    //returns whether a point is on a plane or not
    //additionaly it returns the position of the point on the plane
    fun isPointOnPlane(row: Int, col: Int): Pair<Boolean, Int> {
        val idx = m_listPlanePoints.indexOf(Coordinate2D(row, col))
        return if (idx < 0) Pair.create(false, idx) else Pair.create(true, idx)
    }

    /***
     * computes the list of plane points
     * @param[in] - sendSignal, whether to send signal that a new configuration was computed
     */
    //computes all the points on a plane
    //and returns false if planes intersect and true otherwise
    //also detects if a plane lies outside of the grid
    //also marks to which plane does the point belong and wether is a plane head or not
    fun computePlanePointsList(): Boolean {
        m_listPlanePoints.clear()
        m_listPlanePointsAnnotations.clear()
        var returnValue = true
        isPlaneOutsideGrid = false
        for (i in m_planeList.indices) {
            val pl = m_planeList[i]
            val ppi = PlanePointIterator(pl)
            var isHead = true
            while (ppi.hasNext()) {
                val qp = ppi.next()
                if (!isPointInGrid(qp)) isPlaneOutsideGrid = true
                ///compute the point's annotation
                val annotation = generateAnnotation(i, isHead)
                val isOnPlane = isPointOnPlane(qp.x(), qp.y())
                if (!isOnPlane.first) {
                    m_listPlanePoints.add(qp.clone() as Coordinate2D)
                    m_listPlanePointsAnnotations.add(annotation)
                } else {
                    returnValue = false
                    m_listPlanePointsAnnotations[isOnPlane.second] = m_listPlanePointsAnnotations[isOnPlane.second] or annotation
                }
                isHead = false
            }
        }
        m_PlanesOverlap = !returnValue
        return returnValue
    }

    //returns the size of the plane list
    val planeListSize: Int
        get() = m_planeList.size

    //generates a random position on the grid
    fun generateRandomGridPosition(): Coordinate2D {
        val idx = generateRandomNumber(rowNo * colNo)
        return Coordinate2D(idx % rowNo, idx / rowNo)
    }

    //finds how good is a guess
    fun getGuessResult(qp: Coordinate2D): Type {
        if (isPointHead(qp.x(), qp.y())) return Type.Dead
        return if (isPointOnPlane(qp.x(), qp.y()).first) Type.Hit else Type.Miss
    }

    fun rotatePlane(idx: Int): Boolean {
        if (idx < 0 || idx >= m_planeList.size) return false
        val pl = m_planeList[idx]
        pl.rotate()
        computePlanePointsList()
        return true
    }

    fun movePlaneUpwards(idx: Int): Boolean {
        if (idx < 0 || idx >= m_planeList.size) return false
        val pl = m_planeList[idx]
        pl.translateWhenHeadPosValid(0, -1, rowNo, colNo)
        computePlanePointsList()
        return true
    }

    fun movePlaneDownwards(idx: Int): Boolean {
        if (idx < 0 || idx >= m_planeList.size) return false
        val pl = m_planeList[idx]
        pl.translateWhenHeadPosValid(0, 1, rowNo, colNo)
        computePlanePointsList()
        return true
    }

    fun movePlaneLeft(idx: Int): Boolean {
        if (idx < 0 || idx >= m_planeList.size) return false
        val pl = m_planeList[idx]
        pl.translateWhenHeadPosValid(-1, 0, rowNo, colNo)
        computePlanePointsList()
        return true
    }

    fun movePlaneRight(idx: Int): Boolean {
        if (idx < 0 || idx >= m_planeList.size) return false
        val pl = m_planeList[idx]
        pl.translateWhenHeadPosValid(1, 0, rowNo, colNo)
        computePlanePointsList()
        return true
    }

    fun doPlanesOverlap(): Boolean {
        return m_PlanesOverlap
    }

    fun isPointInGrid(qp: Coordinate2D): Boolean {
        if (qp.x() < 0 || qp.y() < 0) return false
        return if (qp.x() >= colNo || qp.y() >= rowNo) false else true
    }

    ///for integration with QML
    fun planesPointsCount(): Int {
        return m_listPlanePoints.size
    }

    fun getPlanePoint(idx: Int): Coordinate2D {
        return m_listPlanePoints[idx]
    }

    //retrieves additional information about a plane point
    //the plane idx, whether it is a plane head or not
    fun getPlanePointAnnotation(idx: Int): Int {
        return m_listPlanePointsAnnotations[idx]
    }

    //transforms the annotation in a list of plane ids
    fun decodeAnnotation(annotation: Int): Vector<Int> {
        val retVal = Vector<Int>()
        for (i in 0 until planeNo) {
            //int mask = 0x3 << (2 * i);
            val mask1 = 0x1 shl 2 * i
            val mask2 = 0x2 shl 2 * i
            /*if (mask & annotation)
            retVal.push_back(i);*/if (mask1 and annotation > 0) retVal.add(i)
            if (mask2 and annotation > 0) retVal.add(-i - 1)
        }
        return retVal
    }

    //generates a plane at a random position on the grid
    protected fun generateRandomPlane(): Plane {
        val qp = generateRandomGridPosition()
        val orient = generateRandomPlaneOrientation()
        return Plane(qp, orient)
    }

    //generates a random plane orientation
    protected fun generateRandomPlaneOrientation(): Orientation {
        val idx = generateRandomNumber(4)
        return when (idx) {
            0 -> Orientation.NorthSouth
            1 -> Orientation.SouthNorth
            2 -> Orientation.EastWest
            3 -> Orientation.WestEast
            else -> Orientation.NorthSouth
        }
    }

    //randomly generates grid with planes
    protected fun initGridByAutomaticGeneration(): Boolean {
        var count = 0
        val listPossiblePositions = Vector<Plane>()

        //build a list of all possible positions
        //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
        for (i in 0 until rowNo) for (j in 0 until colNo) for (k in 0..3) {
            val pl = Plane(i, j, Orientation.values()[k])
            listPossiblePositions.add(pl)
        }
        while (count < planeNo) {
            //generate a list iterator
            val it = listPossiblePositions.iterator()
            //elimintate all positions that are valid considering the already
            //created planes
            while (it.hasNext()) {
                val pl = it.next()
                //if the plane is outside of the grid remove the position
                if (!isPlanePosValid(pl)) {
                    it.remove()
                    continue
                }
                //if the plane is already in the list remove the position
                //also save the plane in the list of planes
                if (!savePlane(pl)) {
                    it.remove()
                    continue
                }

                //compute all the points on planes and check for intersections
                if (!computePlanePointsList()) {
                    removePlane(pl)
                    it.remove()
                    continue
                } else {
                    removePlane(pl)
                }
            }

            //if no positions are left in the list return false
            if (listPossiblePositions.isEmpty()) return false

            //from the positions that are left in the list
            //choose a random one
            val pos = generateRandomNumber(listPossiblePositions.size)
            val pl = listPossiblePositions[pos]
            //save the selected plane
            if (savePlane(pl)) count++
        } //while
        return true
    }

    //removes a given plane from the list of planes
    fun removePlane(pl: Plane) {
        m_planeList.remove(pl)
    }

    //returns whether a point is head of a plane or not
    protected fun isPointHead(row: Int, col: Int): Boolean {
        return if (searchPlane(row, col) != -1) true else false
    }

    //verifies if a plane position is valid within the grid
    protected fun isPlanePosValid(pl: Plane): Boolean {
        return pl.isPositionValid(rowNo, colNo)
    }

    //generates annotation for one point on a given plane
    //this is not the final annotation of the point
    //when it belongs to more planes the function is called
    //more times and the results are combined
    fun generateAnnotation(planeNo: Int, isHead: Boolean): Int {
        var annotation = 1
        var bitsShifted = 2 * planeNo
        if (isHead) bitsShifted++
        annotation = annotation shl bitsShifted
        return annotation
    }

    //gets the plane points of the plane with index pos except for the head
    fun getPlanePoints(pos: Int): Pair<Boolean, Vector<Coordinate2D>> {
        if (pos < 0 || pos >= m_planeList.size) return Pair.create(false, Vector())
        val pl = m_planeList[pos]
        return Pair.create(true, pl.planePoints())
    }

    fun addGuess(gp: GuessPoint) {
        guesses.add(gp.clone() as GuessPoint)
    }

    //for unit tests
    fun setPlanePoints(list: Vector<Coordinate2D>) {
        m_listPlanePoints = list
    }
}