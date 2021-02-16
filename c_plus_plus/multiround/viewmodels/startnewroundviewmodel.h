#ifndef __START_NEW_ROUND_VIEWMODEL__
#define __START_NEW_ROUND_VIEWMODEL__


#include <QString>
#include <QJsonObject>


struct StartNewRoundViewModel {
    long int m_GameId;
    long int m_OwnUserId;
    long int m_OpponentUserId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("ownUserId", QString::number(m_OwnUserId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        return retVal;
    }
};

















#endif
