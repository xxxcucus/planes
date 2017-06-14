#include "plane.h"
#include "planeiterators.h"
#include <QPoint>
#include <QString>
#include <QDebug>


//Various constructors
Plane::Plane()
{
    m_row = 0;
    m_col = 0;
    m_orient = NorthSouth;
}


Plane::Plane(int row, int col, Orientation orient)
{
    m_row = row;
    m_col = col;
    m_orient = orient;
}

Plane::Plane(const QPoint &qp, Orientation orient)
{
    m_row = qp.x();
    m_col = qp.y();
    m_orient = orient;
}

//equality operator
bool Plane::operator==(const Plane &pl1) const
{
    return ((pl1.m_row == m_row) && (pl1.m_col == m_col) && (pl1.m_orient == m_orient));
}
//assignment operator
void Plane::operator =(const Plane &pl1)
{
    m_row = pl1.m_row;
    m_col = pl1.m_col;
    m_orient = pl1.m_orient;
}

//Clockwise 90 degrees rotation of the plane
void Plane::rotate()
{
    switch(m_orient)
    {
    case NorthSouth:
        m_orient = EastWest;
        break;
    case EastWest:
        m_orient = SouthNorth;
        break;
    case SouthNorth:
        m_orient = WestEast;
        break;
    case WestEast:
        m_orient = NorthSouth;
        break;
    default:
        return;
    }
}

//checks to see if a plane contains a certain point
//uses a PlanePointIterator which enumerates
//all the points on the plane
bool Plane::containsPoint(const QPoint &qp) const
{
    PlanePointIterator ppi(*this);

    while(ppi.hasNext())
    {
        QPoint qp1=ppi.next();
        if(qp==qp1)
            return true;
    }

    return false;
}

//Checks to see if the plane is
//in its totality inside a grid
//of size row X col
bool Plane::isPositionValid(int row, int col) const
{
    PlanePointIterator ppi(*this);

    while(ppi.hasNext())
    {
        QPoint qp = ppi.next();
        if(qp.x()<0 || qp.x()>=row)
            return false;
        if(qp.y()<0 || qp.y()>=col)
            return false;
    }

    return true;

}

//utility function
//generates a random number
int Plane::generateRandomNumber(int valmax)
{
    double rnd = rand()/ static_cast<double>(RAND_MAX);
    if (rnd==1.0)
        rnd = 0.5;
    int val = static_cast<double>(valmax)*rnd;

    return val;
}

//constructs a string representation of a plane
QString Plane::toString() const
{
    QString toReturn = "";

    toReturn += "Plane head: ";
    toReturn += QString::number(m_row);
    toReturn += "-";
    toReturn += QString::number(m_col);
    toReturn += " oriented: ";

    switch(m_orient)
    {
    case NorthSouth:
        toReturn+= "NorthSouth";
        break;
    case SouthNorth:
        toReturn+= "SouthNorth";
        break;
    case EastWest:
        toReturn+= "EastWest";
        break;
    case WestEast:
        toReturn+= "WestEast";
        break;
    default:
        ;
    }

    return toReturn;
}

void Plane::translateWhenHeadPosValid(int offsetX, int offsetY, int row, int col)
{
    if ((m_row + offsetX < 0) || (m_row + offsetX >= row)) {
        return;
    }

    if ((m_col + offsetY < 0) || (m_col + offsetY >= col)) {
        return;
    }

    m_row += offsetX;
    m_col += offsetY;
}

//implements plane translation
Plane Plane::operator+ (const QPoint &qp)
{
    return Plane(this->m_row+qp.x(), this->m_col+qp.y(), this->m_orient);
}

