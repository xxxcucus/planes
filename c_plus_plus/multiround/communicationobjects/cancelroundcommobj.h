#ifndef __CANCEL_ROUND_COMMOBJ__
#define __CANCEL_ROUND_COMMOBJ__

#include "basiscommobj.h"
#include "viewmodels/cancelroundviewmodel.h"
class MultiplayerRound;

class CancelRoundCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    CancelRoundCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}
    
    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
    
protected:
    CancelRoundCommObj() {}

public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();

private:
    CancelRoundViewModel prepareViewModel();
    
private:
    MultiplayerRound* m_MultiRound;
    
    friend class CancelRoundCommObjTest;

};


















#endif
