#include "planesgswindow.h"

PlanesGSWindow::PlanesGSWindow(QWidget *parent) : QMainWindow(parent)
{
    //builds the game object - the controller
    mRound = new PlaneRound(10, 10, 3);

    m_UserData = new UserData();    
    
    //builds the view object
    mPlanesView = new PlanesGSView(mRound, m_UserData);
    setCentralWidget(mPlanesView);


}
