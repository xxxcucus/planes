#ifndef __CONNECT_TO_GAME_COMMOBJ__
#define __CONNECT_TO_GAME_COMMOBJ__


#include "basiscommobj.h"

class ConnectToGameCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    ConnectToGameCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void gameConnectedTo(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    
private:
    QString m_GameName;
};















#endif
