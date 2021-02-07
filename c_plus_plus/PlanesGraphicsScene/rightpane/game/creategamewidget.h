#ifndef __CREATE_GAME_WIDGET__
#define __CREATE_GAME_WIDGET__

#include <QFrame>
#include <QLineEdit>
#include <QNetworkReply>
#include <QNetworkAccessManager>
#include <QSettings>

#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"


class CreateGameWidget : public QFrame
{
    Q_OBJECT

public:
    CreateGameWidget(GlobalData* globalData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, MultiplayerRound* mrd, QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); TODO

public slots:
    void createGameSlot();
    void connectToGameSlot();
    void errorCreateGame(QNetworkReply::NetworkError code);
    void finishedCreateGame();    
    void errorConnectToGame(QNetworkReply::NetworkError code);
    void finishedConnectToGame();    

    
signals:
    void gameConnectedTo(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);

private:
    bool validateCreateGameReply(const QJsonObject& reply);
    
public:
    QLineEdit* m_GameName;
    QNetworkReply* m_CreateGameReply = nullptr;
    QNetworkReply* m_ConnectToGameReply = nullptr;
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
};








#endif
