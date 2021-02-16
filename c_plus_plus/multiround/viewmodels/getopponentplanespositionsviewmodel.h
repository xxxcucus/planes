#ifndef __GIVE_OPPONENTS_PLANES_POSITIONS__
#define __GIVE_OPPONENTS_PLANES_POSITIONS__

#include <QJsonObject>

struct GetOpponentsPlanesPositionsViewModel {
    
    long int m_GameId;
    long int m_RoundId;
    long int m_OwnUserId;
    long int m_OpponentUserId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("ownUserId", QString::number(m_OwnUserId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        return retVal;
    }
};


#endif
