#ifndef PLANE_H
#define PLANE_H

#include <string>
#include "vectoriterator.h"
#include "coordinate2d.h"

//Describes a plane on a grid

class Plane
{
public:
    enum Orientation { NorthSouth = 0, SouthNorth = 1, WestEast = 2, EastWest = 3 };

private:
    //plane orientation
    Orientation m_orient;
    //coordinates of the position of the head of the plane
    int m_row, m_col;

public:
    //Various constructors
    Plane();
    Plane(int row, int col, Orientation orient);
    Plane(const PlanesCommonTools::Coordinate2D& qp, Orientation orient);

    //setter and getters
    //gives the planes orientation
    Orientation orientation() const {return m_orient; }
    //gives the plane head's row and column
    int row() const { return m_row; }
    int col() const { return m_col;}
    //sets the plane head position
    void row(int row) { m_row = row; }
    void col(int col) { m_col = col; }
    void orientation(Orientation orient) { m_orient = orient; }
    //gives the coordinates of the plane head
    PlanesCommonTools::Coordinate2D head() const { return PlanesCommonTools::Coordinate2D(m_row, m_col); }

    //operators
    //compares two planes
    bool operator==(const Plane& pl1) const;
    //translates a plane by a 2d translation vector
    Plane operator+(const PlanesCommonTools::Coordinate2D& qp);

    //geometrical transformations
    //clockwise rotation of planes
    void rotate();
    //translation with given offset in a grid with row and col rows and columns
    //if the future head position is not valid do not translate
    void translateWhenHeadPosValid(int offsetX, int offsetY, int row, int col);

    //other utility functions
    //tests whether a poaint is a plane's head
    bool isHead(const PlanesCommonTools::Coordinate2D& qp) const { return qp == head(); }
    //checks if a certain point on the grid is on the plane
    bool containsPoint(const PlanesCommonTools::Coordinate2D& qp) const;
    //returns whether a plane position is valid (the plane is completely contained inside the grid) in a grid with row and col
    bool isPositionValid(int row, int col) const;
    //generates a random number from 0 and valmax-1
    static int generateRandomNumber(int valmax);
    //displays the plane
    std::string toString() const;
	std::vector<PlanesCommonTools::Coordinate2D> getPlanePoints() const;
};



#endif // PLANE_H
