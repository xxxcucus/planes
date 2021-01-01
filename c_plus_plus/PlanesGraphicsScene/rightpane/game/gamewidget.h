#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gameinfo.h"
#include "userdata.h"


class GameWidget : public QWidget {
    
public:
    GameWidget(UserData* userData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, QWidget* parent = nullptr);
    
private:    
    UserData* m_UserData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    
};

#endif
