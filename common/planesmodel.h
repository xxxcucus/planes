#ifndef PLANESMODEL_H
#define PLANESMODEL_H

#include "planegrid.h"
#include "computerlogic.h"


//this is the main model object
//it keeps the size of the grid
//the number of planes
//2 PlaneGrid objects and one ComputerLogic object
//its responsabilities are to create and
//destroy the plane grids and computerlogic
class PlanesModel
{
    //size of the grid and number of planes
    int m_rowNo;
    int m_colNo;
    int m_planeNo;

    //PlaneGrid objects manage the logic of a set of planes on a grid
    //as well as various operations: save, remove, search, etc.
    PlaneGrid* m_playerGrid;
    PlaneGrid* m_computerGrid;

    //ComputerLogic is the object that keeps the
    //computer's strategy
    ComputerLogic* m_computerLogic;

public:
    PlanesModel(int rowNo, int colNo, int planeNo);
    ~PlanesModel();

    PlaneGrid* playerGrid()  { return m_playerGrid; }
    PlaneGrid* computerGrid()  { return m_computerGrid; }

    ComputerLogic* computerLogic()  { return m_computerLogic; }
};

#endif // PLANESMODEL_H
