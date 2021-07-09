package com.planes.common;

import java.util.Random;
import java.util.Vector;

public class Plane implements Cloneable {

    //Various constructors
    public Plane() {
        m_row = 0;
        m_col = 0;
        m_orient = Orientation.NorthSouth;
    }
    public Plane(int row, int col, Orientation orient) {
        m_row = row;
        m_col = col;
        m_orient = orient;
    }
    public Plane(final Coordinate2D qp, Orientation orient) {
        m_row = qp.x();
        m_col = qp.y();
        m_orient = orient;
    }

    //setter and getters
    //gives the planes orientation
    public Orientation orientation() {return m_orient; }
    //gives the plane head's row and column
    public int row()  { return m_row; }
    public int col() { return m_col;}
    //sets the plane head position
    public void row(int row) { m_row = row; }
    public void col(int col) { m_col = col; }
    public void orientation(Orientation orient) { m_orient = orient; }
    //gives the coordinates of the plane head
    public Coordinate2D head()  { return new Coordinate2D(m_row, m_col); }

    //operators
    //compares two planes
    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        Plane plane = (Plane)other;

        return ((plane.m_row == m_row) && (plane.m_col == m_col) && (plane.m_orient == m_orient));
    }
    //translates a plane by a 2d translation vector
    public Plane add(final Coordinate2D qp) {
        return new Plane(m_row + qp.x(), m_col + qp.y(), m_orient);
    }

    //geometrical transformations
    //clockwise rotation of planes
    public void rotate() {
        switch(m_orient)
        {
            case NorthSouth:
                m_orient = Orientation.EastWest;
                break;
            case EastWest:
                m_orient = Orientation.SouthNorth;
                break;
            case SouthNorth:
                m_orient = Orientation.WestEast;
                break;
            case WestEast:
                m_orient = Orientation.NorthSouth;
                break;
            default:
                return;
        }
    }
    //translation with given offset in a grid with row and col rows and columns
    //if the future head position is not valid do not translate
    public void translateWhenHeadPosValid(int offsetX, int offsetY, int row, int col) {
        if ((m_row + offsetX < 0) || (m_row + offsetX >= row)) {
            return;
        }

        if ((m_col + offsetY < 0) || (m_col + offsetY >= col)) {
            return;
        }

        m_row += offsetX;
        m_col += offsetY;
    }

    //other utility functions
    //tests whether a poaint is a plane's head
    public boolean isHead(final Coordinate2D qp)  { return qp.equals(head()); }
    //checks if a certain point on the grid is on the plane
    public boolean containsPoint(final Coordinate2D qp) {
        PlanePointIterator ppi = new PlanePointIterator(this);

        while(ppi.hasNext())
        {
            Coordinate2D qp1 = ppi.next();
            if(qp.equals(qp1))
                return true;
        }

        return false;
    }
    //returns whether a plane position is valid (the plane is completely contained inside the grid) in a grid with row and col
    public boolean isPositionValid(int row, int col) {
        PlanePointIterator ppi = new PlanePointIterator(this);

        while(ppi.hasNext())
        {
            Coordinate2D qp = ppi.next();
            if(qp.x() < 0 || qp.x() >= row)
                return false;
            if(qp.y() < 0 || qp.y() >= col)
                return false;
        }

        return true;
    }
    //generates a random number from 0 and valmax-1
    public static int generateRandomNumber(int valmax) {
        return m_Random.nextInt(valmax);
    }
    //displays the plane
    @Override
    public String toString() {
        String toReturn = "";

        toReturn = toReturn + "Plane head: ";
        toReturn = toReturn + m_row;
        toReturn = toReturn + "-";
        toReturn = toReturn + m_col;
        toReturn = toReturn + " oriented: ";

        switch(m_orient)
        {
            case NorthSouth:
                toReturn = toReturn + "NorthSouth";
                break;
            case SouthNorth:
                toReturn = toReturn + "SouthNorth";
                break;
            case EastWest:
                toReturn = toReturn + "EastWest";
                break;
            case WestEast:
                toReturn = toReturn + "WestEast";
                break;
            default:
                ;
        }

        return toReturn;
    }

    @Override
    public Object clone() {
        return new Plane(m_row, m_col, m_orient);
    }

    Vector<Coordinate2D> getPlanePoints() {
        PlanePointIterator ppi = new PlanePointIterator(this);
        Vector<Coordinate2D> retVal = new Vector<Coordinate2D>();
        ppi.next();  //do not return the head
        while (ppi.hasNext()) {
            retVal.add((Coordinate2D)ppi.next().clone());
        }
        return retVal;
    }


    //plane orientation
    private Orientation m_orient;
    //coordinates of the position of the head of the plane
    private int m_row;
    private int m_col;

    private static Random m_Random = new Random();
}
