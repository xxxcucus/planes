#ifndef PLANE_H
#define PLANE_H

#include <string>
#include <vector>
#include "coordinate2d.h"

//Describes a plane on a grid

class Plane
{
public:
    enum Orientation { NorthSouth = 0, SouthNorth = 1, WestEast = 2, EastWest = 3 };

    static constexpr PlanesCommonTools::Coordinate2D pointsNorthSouth[] = { PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(0, 1), PlanesCommonTools::Coordinate2D(-1, 1),
                                                                PlanesCommonTools::Coordinate2D(1, 1), PlanesCommonTools::Coordinate2D(-2, 1), PlanesCommonTools::Coordinate2D(2, 1),
                                                                PlanesCommonTools::Coordinate2D(0, 2),PlanesCommonTools::Coordinate2D(0, 3), PlanesCommonTools::Coordinate2D(-1, 3),
                                                                PlanesCommonTools::Coordinate2D(1, 3) };

    static constexpr PlanesCommonTools::Coordinate2D pointsSouthNorth[] = { PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(0, -1), PlanesCommonTools::Coordinate2D(-1, -1),
                                                                PlanesCommonTools::Coordinate2D(1, -1), PlanesCommonTools::Coordinate2D(-2, -1), PlanesCommonTools::Coordinate2D(2, -1),
                                                                PlanesCommonTools::Coordinate2D(0, -2), PlanesCommonTools::Coordinate2D(0, -3), PlanesCommonTools::Coordinate2D(-1, -3),
                                                                PlanesCommonTools::Coordinate2D(1, -3) };

    static constexpr PlanesCommonTools::Coordinate2D pointsEastWest[] = { PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(1, 0), PlanesCommonTools::Coordinate2D(1, -1),
                                                              PlanesCommonTools::Coordinate2D(1, 1), PlanesCommonTools::Coordinate2D(1, -2), PlanesCommonTools::Coordinate2D(1, 2),
                                                              PlanesCommonTools::Coordinate2D(2, 0), PlanesCommonTools::Coordinate2D(3, 0), PlanesCommonTools::Coordinate2D(3, -1),
                                                              PlanesCommonTools::Coordinate2D(3, 1) };

    static constexpr PlanesCommonTools::Coordinate2D pointsWestEast[] = { PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(-1, 0), PlanesCommonTools::Coordinate2D(-1, -1),
                                                              PlanesCommonTools::Coordinate2D(-1, 1), PlanesCommonTools::Coordinate2D(-1, -2), PlanesCommonTools::Coordinate2D(-1, 2),
                                                              PlanesCommonTools::Coordinate2D(-2, 0), PlanesCommonTools::Coordinate2D(-3, 0), PlanesCommonTools::Coordinate2D(-3, 1),
                                                              PlanesCommonTools::Coordinate2D(-3, -1) };

private:
    //plane orientation
    Orientation m_orient;
    //coordinates of the position of the head of the plane
    int m_row, m_col;

public:
    //Various constructors
    constexpr Plane() : m_row(0), m_col(0), m_orient(NorthSouth) {}
    constexpr Plane(int row, int col, Orientation orient) : m_row(row), m_col(col), m_orient(orient) {}
    constexpr Plane(const PlanesCommonTools::Coordinate2D& qp, Orientation orient) : m_row(qp.x()), m_col(qp.y()), m_orient(orient) {}
    constexpr Plane(const Plane& pl) : m_row(pl.m_row), m_col(pl.m_col), m_orient(pl.m_orient) {}


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

    constexpr bool containsPointConstExpr(const PlanesCommonTools::Coordinate2D& qp) const {

        if (m_orient == Orientation::NorthSouth) {
            for (int i = 0; i < 10; i++) {
                if (pointsNorthSouth[i] + PlanesCommonTools::Coordinate2D(m_row, m_col) == qp)
                    return true;
            }
            return false;
        }

        if (m_orient == Orientation::SouthNorth) {
            for (int i = 0; i < 10; i++) {
                if (pointsSouthNorth[i] + PlanesCommonTools::Coordinate2D(m_row, m_col) == qp)
                    return true;
            }
            return false;
        }

        if (m_orient == Orientation::EastWest) {
            for (int i = 0; i < 10; i++) {
                if (pointsEastWest[i] + PlanesCommonTools::Coordinate2D(m_row, m_col) == qp)
                    return true;
            }
            return false;
        }

        if (m_orient == Orientation::WestEast) {
            for (int i = 0; i < 10; i++) {
                if (pointsWestEast[i] + PlanesCommonTools::Coordinate2D(m_row, m_col) == qp)
                    return true;
            }
            return false;
        }

        return false;
    }
    constexpr bool containsPointDummy(const PlanesCommonTools::Coordinate2D& qp) const  {
        return true;
    }

    //returns whether a plane position is valid (the plane is completely contained inside the grid) in a grid with row and col
    bool isPositionValid(int row, int col) const;
    //generates a random number from 0 and valmax-1
    static int generateRandomNumber(int valmax);
    //displays the plane
    std::string toString() const;
	std::vector<PlanesCommonTools::Coordinate2D> getPlanePoints() const;
};



#endif // PLANE_H
