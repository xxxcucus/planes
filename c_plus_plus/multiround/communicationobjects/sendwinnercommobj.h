#ifndef __SEND_WINNER_COMMOBJ__
#define __SEND_WINNER_COMMOBJ__


#include "basiscommobj.h"

class SendWinnerCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    SendWinnerCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(bool draw, long int winnerId);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
};

#endif 
