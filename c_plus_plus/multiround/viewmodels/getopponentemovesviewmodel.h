#ifndef __GET_OPPONENT_MOVES_VIEWMODEL__
#define __GET_OPPONENT_MOVES_VIEWMODEL__

#include <QJsonObject>
#include "basisrequestviewmodel.h"

struct GetOpponentsMovesViewModel: BasisRequestViewModel {
    long int m_GameId = 0L;
    long int m_RoundId = 0L;
    long int m_OpponentUserId = 0L;
    int m_MoveIndex;
    
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        retVal.insert("opponentMoveIndex", m_MoveIndex);
        return retVal;
    }
};









#endif
