#ifndef __CREATE_GAME_COMMOBJ__
#define __CREATE_GAME_COMMOBJ__

#include "basiscommobj.h"

class CreateGameCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    CreateGameCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void gameCreated(const QString& gameName, const QString& userName);
    
private:
    QString m_GameName;
};














#endif
