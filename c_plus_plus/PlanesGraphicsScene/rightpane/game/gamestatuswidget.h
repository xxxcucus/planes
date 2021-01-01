#ifndef __GAME_STATUS_WIDGET__
#define __GAME_STATUS_WIDGET__

#include <QFrame>
#include <QLabel>
#include <QNetworkReply>
#include <QSettings>
#include <QNetworkAccessManager>
#include "userdata.h"
#include "gameinfo.h"


class GameStatusWidget : public QFrame
{
    Q_OBJECT

public:
    explicit GameStatusWidget(UserData* userData, QSettings* settings, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); TODO

public slots:
    void gameCreatedSlot(const QString& gameName, const QString& username);
    void gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    void refreshSlot();
    void errorRefreshStatus(QNetworkReply::NetworkError code);
    void finishedRefreshStatus(); 
    
private:
    bool validateRefreshStatusReply(const QJsonObject& reply);
    
public:
    QLabel* m_GameName;
    QLabel* m_FirstPlayerName;
    QLabel* m_SecondPlayerName;
    QLabel* m_RoundName;
    QNetworkReply* m_RefreshGameStatusReply = nullptr;
    
    UserData* m_UserData;
    QSettings* m_Settings;
    QNetworkAccessManager* m_NetworkManager;
    GameInfo* m_GameInfo;
};





#endif
