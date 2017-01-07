#ifndef PLANE_H
#define PLANE_H

#include <QList>
#include <QPoint>
#include "listiterator.h"

//Describes a plane on a grid

using namespace MyIterator;

class Plane
{
public:
    enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};

private:
    //plane orientation
    Orientation m_orient;
    //coordinates of the position of the head of the plane
    int m_row, m_col;

public:
    //Various constructors
    Plane();
    Plane(int row, int col, Orientation orient);
    Plane(const QPoint &qp, Orientation orient);
    //checks if a certain point on the grid is on the plane
    bool containsPoint(const QPoint &qp) const;
    //compares two planes
    bool operator ==(const Plane &pl1) const;
    //equals operator
    void operator =(const Plane &pl1);
    //translates a plane by a QPoint
    Plane operator+(const QPoint &qp);
    //clockwise rotation of planes
    void rotate();
    //returns whether a plane position is valid in a grid with row and col
    bool isPositionValid(int row, int col) const;
    //generates a random number from 0 and valmax-1
    static int generateRandomNumber(int valmax);
    //displays the plane
    QString toString() const;
    //gives the coordinates of the plane head
    QPoint head() const {return QPoint(m_row,m_col); }
    //tests whether a QPoint is a planes head
    bool isHead(const QPoint &qp) const {return qp==head();}
    //gives the planes orientation
    Orientation orientation() const {return m_orient; }
    //gives the plane row and column
    int row() const {return m_row; }
    int col() const {return m_col;}
    //sets the plane row and column
    void row(int row) {m_row = row; }
    void col(int col) {m_col = col; }
    void orientation(Orientation orient) {m_orient = orient; }
};





//iterates over the points that make a plane
class PlanePointIterator : public ListIterator<QPoint>
{
    Plane m_plane;
public:
    PlanePointIterator(const Plane &pl);

private:
    void generateList();

};

//lists the relatives positions of all planes that pass through the point (0,0)
class PlaneIntersectingPointIterator: public ListIterator<Plane>
{
    QPoint m_point;

public:        

    //constructor taking a QPoint
    PlaneIntersectingPointIterator(const QPoint &qp=QPoint(0,0));

private:
    //generates list of plane indexes that pass through (0,0)
    void generateList();

};


//lists the points that can influence the value of a point
class PointInfluenceIterator: ListIterator<QPoint>
{
    QPoint m_point;

public:
    //constructor
    PointInfluenceIterator(const QPoint &qp=QPoint(0,0));

private:
    //generates the list points influencing the point (0,0)
    void generateList();
};



//describes a guess together with its result Miss,Hit or Dead
class GuessPoint
{
public:
    enum Type {Miss, Hit, Dead};

    //the coordinates of the guess
    int m_row, m_col;
    //the result of the guess
    Type m_type;

public:
    //various constructors
    GuessPoint(int row, int col);
    GuessPoint(int row, int col, Type tp);

    //sets the result of the guess
    void setType(Type tp) {m_type = tp;}
    //tests to see whether a point lies in a list of guesses
    //static bool isGuess(QPoint qp, const QList <GuessPoint>& guessList);

    //compares two guess points
    bool operator ==(const GuessPoint &pl1) const;

    bool isDead() const {return m_type == Dead;}
    bool isHit() const {return m_type == Hit;}
    bool isMiss() const {return m_type == Miss;}

};

#endif // PLANE_H
