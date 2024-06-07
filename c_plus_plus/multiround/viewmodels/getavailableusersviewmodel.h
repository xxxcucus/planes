#ifndef __GET_AVAILABLE_USERS_VIEW_MODEL__
#define __GET_AVAILABLE_USERS_VIEW_MODEL__

#include "basisrequestviewmodel.h"

#include <QString>
#include <QJsonObject>

struct GetAvailableUsersViewModel: public BasisRequestViewModel {
    int m_LastLoginDay;

    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("lastLoginDay", QString::number(m_LastLoginDay));
        return retVal;
    }
};

#endif
