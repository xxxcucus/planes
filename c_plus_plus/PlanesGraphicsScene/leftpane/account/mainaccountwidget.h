#ifndef _MAIN_ACCOUNT_WIDGET__
#define _MAIN_ACCOUNT_WIDGET__

#include <QWidget>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QJsonObject>
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"
#include "userprofileframe.h"
#include "loginregisterform.h"

class MainAccountWidget : public QWidget {
    Q_OBJECT
    
public:    

    MainAccountWidget(QSettings* settings, GlobalData* globalData, QNetworkAccessManager* m_NetworkManager, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent = nullptr);
    
private:
    UserProfileFrame* m_userProfileFrame;
    LoginRegisterForm* m_loginRegisterForm;
    
    
    GlobalData* m_GlobalData;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    GameInfo* m_GameInfo;
    MultiplayerRound* m_MultiRound;

};


#endif
