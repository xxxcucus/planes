#ifndef __REFRESH_GAME_STATUS_COMMOBJ__
#define __REFRESH_GAME_STATUS_COMMOBJ__


#include "basiscommobj.h"

class RefreshGameStatusCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    RefreshGameStatusCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void refreshStatus(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    
private:
    QString m_GameName;
};



















#endif
