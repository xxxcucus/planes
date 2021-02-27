#ifndef __SEND_MOVE_COMMOBJ__
#define __SEND_MOVE_COMMOBJ__

#include "guesspoint.h"

#include "basiscommobj.h"
class MultiplayerRound;

class SendMoveCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    SendMoveCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}
    
    bool makeRequest(const std::vector<GuessPoint>& guessList, const std::vector<int>& notSentMoves, const std::vector<int>& receivedMoves);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();
    void opponentMoveGenerated(const GuessPoint& gp);
    
private:
    std::vector<int> computeNotReceivedMoves(const std::vector<int>& receivedMoves, int& maxReceivedIndex);
    
private:
    MultiplayerRound* m_MultiRound;
    std::vector<int> m_LastNotSentMoveIndex;
};


















#endif
