#include "planesmodel.h"
#include <ctime>

PlanesModel::PlanesModel(int rowNo, int colNo, int planeNo):
    m_rowNo(rowNo), m_colNo(colNo), m_planeNo(planeNo)
{
    //initializes the random number generator
	time_t timer;
	struct tm y2k = { 0 };
	double seconds = 0.0;
	y2k.tm_hour = 0;   y2k.tm_min = 0; y2k.tm_sec = 0;
	y2k.tm_year = 100; y2k.tm_mon = 0; y2k.tm_mday = 1;
	time(&timer);  /* get current time; same as: timer = time(NULL)  */
	seconds = difftime(timer, mktime(&y2k));
    srand(int(floor(seconds)));

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
