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
    
    bool makeRequest(const std::vector<GuessPoint>& guessList, const std::vector<int>& notSentMoves, const std::vector<int>& receivedMoves, bool fromFinishedSlot = false);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void roundCancelled();
    void opponentMoveGenerated(const GuessPoint& gp);
    void allGuessedAndMovesStillToSend();
    void allMovesSent();
    
private:
    std::vector<int> computeNotReceivedMoves(const std::vector<int>& receivedMoves, int& maxReceivedIndex);
    
private:
    MultiplayerRound* m_MultiRound;
    std::vector<int> m_LastNotSentMoveIndexSucces;  //Not sent moves sent when the controller method on the server was called
    std::vector<int> m_LastNotSentMoveIndexError; //Not sent moves when  the controller method on the server was not called (called too fast after previous call)
};


















#endif
