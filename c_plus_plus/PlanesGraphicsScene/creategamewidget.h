#ifndef __CREATE_GAME_WIDGET__
#define __CREATE_GAME_WIDGET__

#include <QFrame>
#include <QLineEdit>
#include <QNetworkReply>
#include <QNetworkAccessManager>
#include <QSettings>

#include "userdata.h"
#include "gameinfo.h"


class CreateGameWidget : public QFrame
{
    Q_OBJECT

public:
    CreateGameWidget(UserData* userData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); TODO

public slots:
    void createGameSlot();
    void connectToGameSlot();
    void errorCreateGame(QNetworkReply::NetworkError code);
    void finishedCreateGame();    
    
signals:
    void gameCreated(const QString& gameName, const QString& username);

private:
    bool validateCreateGameReply(const QJsonObject& reply);
    
public:
    QLineEdit* m_GameName;
    QNetworkReply* m_CreateGameReply = nullptr;
    QNetworkReply* m_ConnectToGameReply = nullptr;
    UserData* m_UserData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
};








#endif
