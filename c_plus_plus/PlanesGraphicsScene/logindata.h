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
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("username", m_UserName);
        std::string hash = BCrypt::generateHash(std::string(m_Password.toUtf8().data()));
        QString passToSend = QString::fromUtf8(hash.c_str());
        qDebug() << "Pass to send " << passToSend ;
        retVal.insert("password", "$2y$12$NZBLwD5mq.fv3zt7Xt3vyOaUMvUZSYF7ED9xgSrOUdUn0n50d3UvO");
        return retVal;
    }
};

#endif
