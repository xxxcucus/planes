#ifndef __GET_SERVER_VERSION_COMMOBJ__
#define __GET_SERVER_VERSION_COMMOBJ__



#include "basiscommobj.h"

class GetServerVersionCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    GetServerVersionCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;
};












#endif
