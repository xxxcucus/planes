#ifndef __USER_DATA__
#define __USER_DATA__

#include <QString>
#include <QByteArray>


struct UserData {
    QString m_UserName;
    QString m_UserPassword;
    QByteArray m_AuthToken;
};

#endif
