#ifndef __USER_DATA__
#define __USER_DATA__

#include <QString>
#include <QByteArray>


struct UserData {
    QString m_UserName;
    QString m_UserPassword;
    QByteArray m_AuthToken;
    long int m_GameId;
    
public:
    void reset() {
        m_AuthToken = QByteArray();
        m_UserName = QString();
        m_UserPassword = QString();
        m_GameId = 0;
    }
};

#endif
