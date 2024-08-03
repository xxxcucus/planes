#ifndef __USER_WITH_LAST_LOGIN_VIEW_MODEL__
#define __USER_WITH_LAST_LOGIN_VIEW_MODEL__

#include <QString>
#include <QJsonObject>
#include "communicationtools.h"

struct UserWithLastLoginViewModel {
    QString m_UserName;
    long int m_UserId = 0L;
    QDateTime m_LastLogin;

    explicit UserWithLastLoginViewModel(const QJsonObject& jsonObject) {
        m_UserName = jsonObject.value("username").toString();
        m_UserId = jsonObject.value("userid").toString().toLong();
        QString lastLoginString = jsonObject.value("lastLogin").toString();
        m_LastLogin = CommunicationTools::parseDateFromString(lastLoginString);
    }

    UserWithLastLoginViewModel(const QString& username, long int userid) {
        m_UserName = username;
        m_UserId = userid;
        m_LastLogin = QDateTime::currentDateTime();
    }

    UserWithLastLoginViewModel(const QString& username, long int userid, const QDateTime& dateTime) {
        m_UserName = username;
        m_UserId = userid;
        m_LastLogin = dateTime;
    }

private:

    friend bool operator<(const UserWithLastLoginViewModel& user1, const UserWithLastLoginViewModel& user2) {
        if (user1.m_UserName != user2.m_UserName) {
            return user1.m_LastLogin < user2.m_LastLogin;
        }

        return user1.m_LastLogin < user2.m_LastLogin;
    }

    friend bool operator==(const UserWithLastLoginViewModel& user1, const UserWithLastLoginViewModel& user2) {
        return user1.m_LastLogin == user2.m_LastLogin && user1.m_UserName == user2.m_UserName;
    }

};

#endif
