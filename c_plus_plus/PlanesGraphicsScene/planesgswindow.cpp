#include "planesgswindow.h"

PlanesGSWindow::PlanesGSWindow(bool isMultiplayer, QWidget *parent) : QMainWindow(parent)
{
    m_GlobalData = new GlobalData();
    m_GlobalData->reset();
    m_NetworkManager = new QNetworkAccessManager(this);
    m_GameInfo = new GameInfo(isMultiplayer);
    m_Settings = new QSettings("Cristian Cucu", "Planes");


    //builds the game object - the controller
    mRound = new PlaneRound(10, 10, 3);
    m_MultiRound = new MultiplayerRound(10, 10, 3, this, m_NetworkManager, m_GlobalData, m_Settings, m_GameInfo);
    if (isMultiplayer)  //TODO: can the user change the server ?
        m_MultiRound->testServerVersion();
    
    //builds the view object
    mPlanesView = new PlanesGSView(mRound, m_MultiRound, m_GlobalData, m_NetworkManager, m_GameInfo, m_Settings, this);
    setCentralWidget(mPlanesView);
}


PlanesGSWindow::~PlanesGSWindow() {
    delete mRound;
    delete m_MultiRound;
    delete m_GameInfo;
    delete m_Settings;
    delete m_NetworkManager;
}
