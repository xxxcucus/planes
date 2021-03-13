#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include <QFrame>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gameinfo.h"
#include "global/globaldata.h"
#include "multiplayerround.h"

class GameWidget : public QFrame {
    Q_OBJECT
    
public:
    GameWidget(GlobalData* globalData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, MultiplayerRound* mrd, QWidget* parent = nullptr);
     
private:    
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
};

#endif
