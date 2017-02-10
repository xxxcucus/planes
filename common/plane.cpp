#include "plane.h"
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

//constructor
PlanePointIterator::PlanePointIterator(const Plane &pl):
    MyIterator::ListIterator<QPoint>(),
    m_plane(pl)
{
    generateList();
}

//the function that generates the list of points
void PlanePointIterator::generateList()
{

    const QPoint pointsNorthSouth[] = {QPoint(0,0), QPoint(0,1), QPoint(-1,1), QPoint(1,1), QPoint(-2,1), QPoint(2,1), QPoint(0,2),
                                   QPoint(0,3), QPoint(-1,3), QPoint(1,3)};

    const QPoint pointsSouthNorth[] = {QPoint(0,0), QPoint(0,-1), QPoint(-1,-1), QPoint(1,-1), QPoint(-2,-1), QPoint(2,-1), QPoint(0,-2),
                                   QPoint(0,-3), QPoint(-1,-3), QPoint(1,-3)};

    const QPoint pointsEastWest[] = {QPoint(0,0), QPoint(1,0), QPoint(1,-1), QPoint(1,1), QPoint(1,-2), QPoint(1,2), QPoint(2,0),
                                 QPoint(3,0), QPoint(3,-1), QPoint(3,1)};

    const QPoint pointsWestEast[] = {QPoint(0,0), QPoint(-1,0), QPoint(-1,-1), QPoint(-1,1), QPoint(-1,-2), QPoint(-1,2), QPoint(-2,0),
                                 QPoint(-3,0), QPoint(-3,1), QPoint(-3,-1)};


    const int size = 10;

    for(int i=0;i<size;i++)
    {


        switch(m_plane.orientation())
        {
            case Plane::NorthSouth:
                m_internalList << pointsNorthSouth[i]+m_plane.head();
                break;
            case Plane::SouthNorth:
                m_internalList << pointsSouthNorth[i]+m_plane.head();
                break;
            case Plane::WestEast:
                m_internalList << pointsWestEast[i]+m_plane.head();
                break;
            case Plane::EastWest:
                m_internalList << pointsEastWest[i]+m_plane.head();
                break;
            default:
                ;
        }

    }

}


//constructor for the iterator giving all the planes
//passing through the point (0,0)
PlaneIntersectingPointIterator::PlaneIntersectingPointIterator(const QPoint &qp):
    MyIterator::ListIterator<Plane>(),
    m_point(qp)
{
    //generates the list of planes
    generateList();
}


//builds the list of planes that intersect (0,0)
void PlaneIntersectingPointIterator::generateList()
{
    m_internalList.clear();

    //build a list of all possible positions that can possibly contain the (0,0) point
    //enum Orientation {NorthSouth=0, SouthNorth=1, WestEast=2, EastWest=3};
    for(int i=-5+m_point.x();i<6+m_point.x();i++)
        for(int j=-5+m_point.y();j<6+m_point.y();j++)
            for(int k=0;k<4;k++)
            {
                Plane pl(i,j,(Plane::Orientation)k);
                m_internalList.append(pl);
            }

    QMutableListIterator <Plane> i(m_internalList);

    //elimintate all positions that do not contain (0,0)
    while(i.hasNext())
    {
        Plane pl = i.next();

        if(!pl.containsPoint(m_point))
        {
            i.remove();
            continue;
        }

    }


}



PointInfluenceIterator::PointInfluenceIterator(const QPoint &qp):
    MyIterator::ListIterator<QPoint>(),
    m_point(qp)
{
    generateList();
}



void PointInfluenceIterator::generateList()
{
    m_internalList.clear();

    //searches in a range around the selected QPoint
    for(int i=-10+m_point.x();i<10+m_point.y();i++)
        for(int j=-10+m_point.y();j<10+m_point.y();j++)
        {
            QPoint qp(i,j);
            //generates all planes intersecting the point
            PlaneIntersectingPointIterator pipi(qp);

            bool pointFound = false;

            //check to see if any of these planes correspond to the initial point
            while(pipi.hasNext())
            {
                Plane pl= pipi.next();
                if(pl.isHead(m_point))
                {
                    pointFound = true;
                    break;
                }
            }

            if(pointFound)
            {
                m_internalList.push_back(qp);
                continue;
            }
        }
}

//constructor for a guess point
//this one initializes the GuessPoint only partially
//the result of the guess being not known
GuessPoint::GuessPoint(int row, int col):
    m_row(row),
    m_col(col)
{

}

//constructor which initializes the data members of the guess point
GuessPoint::GuessPoint(int row, int col, Type tp):
    m_row(row),
    m_col(col),
    m_type(tp)
{

}

//equals operator
bool GuessPoint::operator==(const GuessPoint &gp1) const
{
    if(m_row==gp1.m_row && m_col==gp1.m_col /*&& m_type==gp1.m_type*/)
        return true;
    else
        return false;
}


