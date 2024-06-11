#ifndef __USER_WITH_LAST_LOGIN_VIEW_MODEL__
#define __USER_WITH_LAST_LOGIN_VIEW_MODEL__

#include <QString>
#include <QJsonObject>

struct UserWithLastLoginViewModel {
    QString m_UserName;
    QDateTime m_LastLogin;

    UserWithLastLoginViewModel(const QJsonObject& jsonObject) {
        m_UserName = jsonObject.value("username").toString();
        QString lastLoginString = jsonObject.value("lastLogin").toString();
        m_LastLogin = parseDateFromString(lastLoginString);
    }

private:
    QDateTime parseDateFromString(const QString& dateString) {
        QStringList dateElements = dateString.split(" ", Qt::SkipEmptyParts);
        int day = 0;
        int month = 0;
        int year = 0;
        if (dateElements.size() > 0)
            day = dateElements[0].toInt();
        if (dateElements.size() > 1)
            month = dateElements[1].toInt();
        if (dateElements.size() > 2)
            year = dateElements[2].toInt();
        QString timeString;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (dateElements.size() > 3) {
            timeString = dateElements[3];
            QStringList timeElements = timeString.split(":", Qt::SkipEmptyParts);
            if (timeElements.size() > 0)
                hour = timeElements[0].toInt();
            if (timeElements.size() > 1)
                minute = timeElements[1].toInt();
            if (timeElements.size() > 2)
                second = timeElements[2].toInt();
        }

        QDate retDate(year, month, day);
        QTime retTime(hour, minute, second);
        return QDateTime(retDate, retTime);
    }

};

#endif
