#ifndef __GAME_VIEW_MODEL__
#define __GAME_VIEW_MODEL__

#include <QString>
#include <QJsonObject>

struct GameViewModel {
    QString m_GameName;
    QString m_Username;
    long int m_UserId;
    long int m_GameId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameName", m_GameName);
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("userId", QString::number(m_UserId));
        retVal.insert("userName", m_Username);
        return retVal;
    }
};









#endif
