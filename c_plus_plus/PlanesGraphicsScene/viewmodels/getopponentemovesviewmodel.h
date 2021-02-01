#ifndef __GET_OPPONENT_MOVES_VIEWMODEL__
#define __GET_OPPONENT_MOVES_VIEWMODEL__

#include <QJsonObject>

struct GetOpponentsMovesViewModel {
    
    long int m_GameId;
    long int m_RoundId;
    long int m_OwnUserId;
    long int m_OpponentUserId;
    int m_MoveIndex;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("ownUserId", QString::number(m_OwnUserId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        retVal.insert("opponentMoveIndex", m_MoveIndex);
        return retVal;
    }
};









#endif
