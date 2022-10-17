#ifndef __SEND_PLANE_POSITIONS_COMM_OBJ_
#define __SEND_PLANE_POSITIONS_COMM_OBJ_

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
class MultiplayerRound;

class MULTIPLAYER_EXPORT SendPlanePositionsCommObj : public BasisCommObj {
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
