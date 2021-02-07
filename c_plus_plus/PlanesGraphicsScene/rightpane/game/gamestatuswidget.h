#ifndef __GAME_STATUS_WIDGET__
#define __GAME_STATUS_WIDGET__

#include <QFrame>
#include <QLabel>
#include <QNetworkReply>
#include <QSettings>
#include <QNetworkAccessManager>
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"


class GameStatusWidget : public QFrame
{
    Q_OBJECT

public:
    explicit GameStatusWidget(GlobalData* globalData, QSettings* settings, QNetworkAccessManager* networkManager, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); TODO

public slots:
    void gameCreatedSlot(const QString& gameName, const QString& username);
    void gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    void refreshSlot();
    void errorRefreshStatus(QNetworkReply::NetworkError code);
    void finishedRefreshStatus(); 
    
signals:
    void gameStatusRefreshed(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);    
    
private:
    bool validateRefreshStatusReply(const QJsonObject& reply);
    
public:
    QLabel* m_GameName;
    QLabel* m_FirstPlayerName;
    QLabel* m_SecondPlayerName;
    QLabel* m_RoundName;
    QNetworkReply* m_RefreshGameStatusReply = nullptr;
    
    GlobalData* m_GlobalData;
    QSettings* m_Settings;
    QNetworkAccessManager* m_NetworkManager;
    GameInfo* m_GameInfo;
    MultiplayerRound* m_MultiRound;
};





#endif
