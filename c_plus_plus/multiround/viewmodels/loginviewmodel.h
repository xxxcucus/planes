#ifndef __LOGIN_VIEW_MODEL__
#define __LOGIN_VIEW_MODEL__

#include <QJsonObject>
#include <QString>
#include <QDebug>
#include "BCrypt.hpp"


struct LoginViewModel {
    QString m_UserName;
    QString m_Password;
    
    public:
    
    QJsonObject toRegisterJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        //qDebug() << "Added username ";
        std::string hash = BCrypt::generateHash(std::string(m_Password.toUtf8().data()));
        //qDebug() << "Generated hash";
        QString passToSend = QString::fromUtf8(hash.c_str());
        //qDebug() << "Pass to send " << passToSend;
        retVal.insert("password", passToSend);
        return retVal;
    }

    QJsonObject toLoginJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        retVal.insert("password", m_Password);
        return retVal;
    }

};

#endif
