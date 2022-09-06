package com.planes.single_player_engine

import java.util.*

class Plane(val row: Int, val col: Int, orient: Orientation) : Cloneable {

    //plane orientation
    private var m_orient = Orientation.NorthSouth

    //coordinates of the position of the head of the plane
    private var m_row = 0
    private var m_col = 0

    init {
        m_row = row
        m_col = col
        m_orient = orient
    }

    //Various constructors
    constructor(): this(0, 0, Orientation.NorthSouth)

    constructor(qp: Coordinate2D, orient: Orientation): this(qp.x(), qp.y(), orient)

    //setter and getters
    //gives the planes orientation
    fun orientation(): Orientation {
        return m_orient
    }

    //gives the plane head's row and column
    fun row(): Int {
        return m_row
    }

    fun col(): Int {
        return m_col
    }

    //sets the plane head position
    fun row(row: Int) {
        m_row = row
    }

    fun col(col: Int) {
        m_col = col
    }

    fun orientation(orient: Orientation) {
        m_orient = orient
    }

    //gives the coordinates of the plane head
    fun head(): Coordinate2D {
        return Coordinate2D(m_row, m_col)
    }

    //operators
    //compares two planes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val plane = other as Plane
        return plane.m_row == m_row && plane.m_col == m_col && plane.m_orient === m_orient
    }

    //translates a plane by a 2d translation vector
    fun add(qp: Coordinate2D): Plane {
        return Plane(m_row + qp.x(), m_col + qp.y(), m_orient)
    }

    //geometrical transformations
    //clockwise rotation of planes
    fun rotate() {
        m_orient = when (m_orient) {
            Orientation.NorthSouth -> Orientation.EastWest
            Orientation.EastWest -> Orientation.SouthNorth
            Orientation.SouthNorth -> Orientation.WestEast
            Orientation.WestEast -> Orientation.NorthSouth
        }
    }

    //translation with given offset in a grid with row and col rows and columns
    //if the future head position is not valid do not translate
    fun translateWhenHeadPosValid(offsetX: Int, offsetY: Int, row: Int, col: Int) {
        if (m_row + offsetX < 0 || m_row + offsetX >= row) {
            return
        }
        if (m_col + offsetY < 0 || m_col + offsetY >= col) {
            return
        }
        m_row += offsetX
        m_col += offsetY
    }

    //other utility functions
    //tests whether a point is a plane's head
    fun isHead(qp: Coordinate2D): Boolean {
        return qp.equals(head())
    }

    //checks if a certain point on the grid is on the plane
    fun containsPoint(qp: Coordinate2D): Boolean {
        val ppi = PlanePointIterator(this)
        while (ppi.hasNext()) {
            val qp1 = ppi.next()
            if (qp.equals(qp1)) return true
        }
        return false
    }

    //returns whether a plane position is valid (the plane is completely contained inside the grid) in a grid with row and col
    fun isPositionValid(row: Int, col: Int): Boolean {
        val ppi = PlanePointIterator(this)
        while (ppi.hasNext()) {
            val qp = ppi.next()
            if (qp.x() < 0 || qp.x() >= row) return false
            if (qp.y() < 0 || qp.y() >= col) return false
        }
        return true
    }

    //displays the plane
    override fun toString(): String {
        var toReturn = ""
        toReturn = toReturn + "Plane head: "
        toReturn = toReturn + m_row
        toReturn = "$toReturn-"
        toReturn = toReturn + m_col
        toReturn = "$toReturn oriented: "
        toReturn = when (m_orient) {
            Orientation.NorthSouth -> toReturn + "NorthSouth"
            Orientation.SouthNorth -> toReturn + "SouthNorth"
            Orientation.EastWest -> toReturn + "EastWest"
            Orientation.WestEast -> toReturn + "WestEast"
        }
        return toReturn
    }

    public override fun clone(): Any {
        return Plane(m_row, m_col, m_orient)
    }

    //do not return the head
    fun planePoints(): Vector<Coordinate2D> {
            val ppi = PlanePointIterator(this)
            val retVal = Vector<Coordinate2D>()
            ppi.next() //do not return the head
            while (ppi.hasNext()) {
                retVal.add(ppi.next().clone() as Coordinate2D)
            }
            return retVal
        }

    override fun hashCode(): Int {
        var result = m_orient.hashCode()
        result = 31 * result + m_row
        result = 31 * result + m_col
        return result
    }


    companion object PlaneStatic {
        //generates a random number from 0 and valmax-1
        fun generateRandomNumber(valmax: Int): Int {
            return m_Random.nextInt(valmax)
        }

        private val m_Random = Random()
    }
}