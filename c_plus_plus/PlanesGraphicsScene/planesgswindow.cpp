#include "planesgswindow.h"

//TODO: add destructor
PlanesGSWindow::PlanesGSWindow(bool isMultiplayer, QWidget *parent) : QMainWindow(parent)
{
    m_GlobalData = new GlobalData();
    m_GlobalData->reset();
    m_NetworkManager = new QNetworkAccessManager(this);
    m_GameInfo = new GameInfo(isMultiplayer);
    m_Settings = new QSettings("Cristian Cucu", "Planes");


    //builds the game object - the controller
    mRound = new PlaneRound(10, 10, 3);
    m_MultiRound = new MultiplayerRound(10, 10, 3, m_NetworkManager, m_GlobalData, m_Settings, m_GameInfo);
    if (isMultiplayer)  //TODO: can the user change the server ?
        m_MultiRound->testServerVersion();
    
    //builds the view object
    mPlanesView = new PlanesGSView(mRound, m_MultiRound, m_GlobalData, m_NetworkManager, m_GameInfo, m_Settings);
    setCentralWidget(mPlanesView);
}
