#ifndef __SEND_WINNER_VIEW_MODEL__
#define __SEND_WINNER_VIEW_MODEL__

#include <QString>
#include <QJsonObject>

struct SendWinnerViewModel {
    long int m_GameId;
    long int m_RoundId;
    long int m_WinnerId;
    bool m_Draw;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("winnerId", QString::number(m_WinnerId));
        retVal.insert("draw", m_Draw);
        return retVal;
    }
};

#endif
