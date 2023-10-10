#ifndef __START_NEW_ROUND_VIEWMODEL__
#define __START_NEW_ROUND_VIEWMODEL__


#include <QString>
#include <QJsonObject>
#include "basisrequestviewmodel.h"


struct StartNewRoundViewModel : public BasisRequestViewModel {
    long int m_GameId;
    long int m_OpponentUserId;
    
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        return retVal;
    }
};

















#endif
