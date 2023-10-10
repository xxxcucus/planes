#ifndef __GAME_VIEW_MODEL__
#define __GAME_VIEW_MODEL__

#include <QString>
#include <QJsonObject>
#include "basisrequestviewmodel.h"

struct GameViewModel: public BasisRequestViewModel {
    QString m_GameName;
    long int m_GameId;
    
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("gameName", m_GameName);
        retVal.insert("gameId", QString::number(m_GameId));
        return retVal;
    }
};









#endif
