#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gameinfo.h"
#include "global/globaldata.h"
#include "multiplayerround.h"

class GameWidget : public QWidget {
    Q_OBJECT
    
public:
    GameWidget(GlobalData* globalData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, MultiplayerRound* mrd, QWidget* parent = nullptr);
 
signals:
    void gameConnectedTo(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    
private:    
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
    
};

#endif
