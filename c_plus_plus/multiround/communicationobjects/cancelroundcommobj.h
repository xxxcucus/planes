#ifndef __CANCEL_ROUND_COMMOBJ__
#define __CANCEL_ROUND_COMMOBJ__

#include "basiscommobj.h"
class MultiplayerRound;

class CancelRoundCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    CancelRoundCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}
    
    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();

    
private:
    MultiplayerRound* m_MultiRound;
    
};


















#endif
