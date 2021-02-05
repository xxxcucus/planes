#ifndef __GIVE_OPPONENTS_PLANES_POSITIONS__
#define __GIVE_OPPONENTS_PLANES_POSITIONS__

#include <QJsonObject>

struct GetOpponentsPlanesPositionsViewModel {
    
    long int m_RoundId;
    long int m_UserId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("playerId", QString::number(m_UserId));      
        return retVal;
    }
};


#endif
