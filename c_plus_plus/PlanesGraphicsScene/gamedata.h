#ifndef __GAME_DATA__
#define __GAME_DATA__

#include <QString>
#include <QJsonObject>

struct GameData {
    QString m_GameName;
    QString m_Username;
    long int m_UserId;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameName", m_GameName);
        QJsonObject userObject1;
        userObject1.insert("username", m_Username);
        userObject1.insert("id", QString::number(m_UserId));
        retVal.insert("firstPlayer", userObject1);
        QJsonObject userObject2;
        userObject2.insert("username", m_Username);
        userObject2.insert("id", QString::number(m_UserId));
        retVal.insert("secondPlayer", userObject2);

        return retVal;
    }
};









#endif
