#ifndef __CONNECT_TO_GAME_COMMOBJ__
#define __CONNECT_TO_GAME_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/gameviewmodel.h"

class MULTIPLAYER_EXPORT ConnectToGameCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    ConnectToGameCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;
    
protected:
    ConnectToGameCommObj() {}

public slots:
    void finishedRequest() override;       
    
signals:
    void gameConnectedTo(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId, bool resetGameScore);
    
private:
    void processResponse(const QJsonObject& retJson);
    GameViewModel prepareViewModel(const QString& gameName);

private:
    QString m_GameName;

    friend class ConnectToGameCommObjTest;
};















#endif
