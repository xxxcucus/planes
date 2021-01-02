#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gameinfo.h"
#include "global/globaldata.h"


class GameWidget : public QWidget {
    
public:
    GameWidget(GlobalData* globalData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, QWidget* parent = nullptr);
    
private:    
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    
};

#endif
