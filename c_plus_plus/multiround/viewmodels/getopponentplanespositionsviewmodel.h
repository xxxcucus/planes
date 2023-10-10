#ifndef __GIVE_OPPONENTS_PLANES_POSITIONS__
#define __GIVE_OPPONENTS_PLANES_POSITIONS__

#include <QJsonObject>
#include "basisrequestviewmodel.h"

struct GetOpponentsPlanesPositionsViewModel: public BasisRequestViewModel {
    
    long int m_GameId;
    long int m_RoundId;
    long int m_OpponentUserId;
    
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        return retVal;
    }
};


#endif
