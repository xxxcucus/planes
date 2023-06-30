#ifndef __LOGOUT_VIEW_MODEL__
#define __LOGOUT_VIEW_MODEL__

#include <QJsonObject>
#include <QString>
#include <QDebug>


struct LogoutViewModel {
    QString m_UserName;

    public:
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        return retVal;
    }

};

#endif
