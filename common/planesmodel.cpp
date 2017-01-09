#include "planesmodel.h"
#include <QTime>

PlanesModel::PlanesModel(int rowNo, int colNo, int planeNo):
    m_rowNo(rowNo), m_colNo(colNo), m_planeNo(planeNo)
{

    //initializes the random number generator
    QTime time = QTime::currentTime();
    int seed = time.msec();
    srand(seed);


    //builds the plane grid objects
    m_playerGrid = new PlaneGrid(m_rowNo, m_colNo, m_planeNo, false);
    m_computerGrid = new PlaneGrid(m_rowNo, m_colNo, m_planeNo, true);

    //builds the computer logic object
    m_computerLogic = new ComputerLogic(m_rowNo, m_colNo, m_planeNo);

}

//deletes the objects
PlanesModel::~PlanesModel()
{
    delete m_computerLogic;
    delete m_computerGrid;
    delete m_playerGrid;
}
