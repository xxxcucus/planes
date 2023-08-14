#ifndef __DEACTIVATE_USER_VIEW_MODEL__
#define __DEACTIVATE_USER_VIEW_MODEL__

#include <QJsonObject>
#include <QString>
#include <QDebug>


struct DeactivateUserViewModel {
    QString m_UserName;

    public:
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        return retVal;
    }

};

#endif

