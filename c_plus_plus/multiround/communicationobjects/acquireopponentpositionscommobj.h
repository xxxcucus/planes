#ifndef __ACQUIRE_OPPONENT_POSITIONS_COMMOBJ__
#define __ACQUIRE_OPPONENT_POSITIONS_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
class MultiplayerRound;

class MULTIPLAYER_EXPORT AcquireOpponentPositionsCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    AcquireOpponentPositionsCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}
    
    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();
    void opponentPlanePositionsReceived();
    
private:
    QString m_GameName;
    MultiplayerRound* m_MultiRound;
};























#endif
