#ifndef __LOGIN_DATA__
#define __LOGIN_DATA__

#include <QJsonObject>
#include <QString>
#include <QDebug>
#include "BCrypt.hpp"


struct LoginData {
    QString m_UserName;
    QString m_Password;
    
    public:
    
    QJsonObject toLoginJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        std::string hash = BCrypt::generateHash(std::string(m_Password.toUtf8().data()));
        QString passToSend = QString::fromUtf8(hash.c_str());
        qDebug() << "Pass to send " << passToSend ;
        retVal.insert("password", "test1");
        return retVal;
    }
    
   QJsonObject toRegisterJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        retVal.insert("password", m_Password);
        return retVal;
    }    
};

#endif
