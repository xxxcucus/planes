#ifndef __GLOBAL_USER_DATA__
#define __GLOBAL_USER_DATA__

#include <QString>
#include <QByteArray>


struct GlobalUserData {
    QString m_UserName;
    QString m_UserPassword;
    QByteArray m_AuthToken;
    long int m_UserId;
    
public:
    void reset() {
        m_AuthToken = QByteArray();
        m_UserName = QString();
        m_UserPassword = QString();
        m_UserId = 0;
    }
};

#endif
