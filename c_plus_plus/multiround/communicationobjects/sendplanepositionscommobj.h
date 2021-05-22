#ifndef __SEND_PLANE_POSITIONS_COMM_OBJ_
#define __SEND_PLANE_POSITIONS_COMM_OBJ_

#include "basiscommobj.h"
class MultiplayerRound;

class SendPlanePositionsCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    SendPlanePositionsCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}
    
    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();
    void opponentPlanePositionsReceived();
    void waitForOpponentPlanePositions();
    
private:
    QString m_GameName;
    MultiplayerRound* m_MultiRound;
};

















#endif
