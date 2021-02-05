#ifndef __CANCEL_ROUND_VIEWMODEL__
#define __CANCEL_ROUND_VIEWMODEL__

#include <QString>
#include <QJsonObject>

struct CancelRoundViewModel {
    long int m_RoundId;
    long int m_GameId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("gameId", QString::number(m_GameId));
        return retVal;
    }
};

























#endif
