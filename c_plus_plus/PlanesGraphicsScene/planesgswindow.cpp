#include "planesgswindow.h"

PlanesGSWindow::PlanesGSWindow(QWidget *parent) : QMainWindow(parent)
{
    //builds the game object - the controller
    mRound = new PlaneRound(10, 10, 3);

    m_GlobalData = new GlobalData();
    m_GlobalData->reset();
    m_NetworkManager = new QNetworkAccessManager(this);
    m_GameInfo = new GameInfo();
    
    //builds the view object
    mPlanesView = new PlanesGSView(mRound, m_GlobalData, m_NetworkManager, m_GameInfo);
    setCentralWidget(mPlanesView);
}
