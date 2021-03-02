#ifndef PLANESGSWINDOW_H
#define PLANESGSWINDOW_H

#include <QMainWindow>
#include <QNetworkAccessManager>
#include <QSettings>
#include "planesgsview.h"
#include "planeround.h"
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"

class PlanesGSWindow : public QMainWindow
{
    Q_OBJECT
public:
    explicit PlanesGSWindow(bool isMultiplayer, QWidget *parent = 0);

signals:

public slots:

private:
    //The view object
    PlanesGSView* mPlanesView;
    //The game object : playing against the computer
    PlaneRound* mRound;
    MultiplayerRound* m_MultiRound;
    //User data for the multiplayer game
    GlobalData* m_GlobalData;
    QNetworkAccessManager* m_NetworkManager;
    GameInfo* m_GameInfo;
    QSettings* m_Settings;
};

#endif // PLANESGSWINDOW_H
