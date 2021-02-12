#ifndef __REQUEST_OPPONENT_MOVES__
#define __REQUEST_OPPONENT_MOVES__


#include "guesspoint.h"

#include "basiscommobj.h"
class MultiplayerRound;

class RequestOpponentMovesCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    RequestOpponentMovesCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}
    
    bool makeRequest(int opponentMoveIndex);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();
    void opponentMoveGenerated(const GuessPoint& gp);
    
private:
    MultiplayerRound* m_MultiRound;
};


















#endif
