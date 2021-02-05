#ifndef __START_NEW_ROUND_VIEWMODEL__
#define __START_NEW_ROUND_VIEWMODEL__


#include <QString>
#include <QJsonObject>


struct StartNewRoundViewModel {
    long int m_GameId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameId", QString::number(m_GameId));
        return retVal;
    }
};

















#endif
