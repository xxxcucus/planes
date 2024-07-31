#ifndef _COMMUNICATION_TOOLS__
#define _COMMUNICATION_TOOLS__

#include <QString>
#include <QNetworkReply>
#include <QJsonObject>
#include <QWidget>

class CommunicationTools {

private:
    static QString localTestServerPath;

public:
    static QNetworkReply* buildPostRequest(const QString& routePath, const QString& serverPath, const QJsonObject& jsonObject, QNetworkAccessManager* networkManager);
    static QNetworkReply* buildPostRequestWithAuth(const QString& routePath, const QString& serverPath, const QJsonObject& jsonObject, const QByteArray& authToken, QNetworkAccessManager* networkManager);
    static QJsonObject objectFromString(const QString& in);
    static void treatCommunicationError(const QString& actionName, QNetworkReply* reply, QWidget* parentWidget);
    static QDateTime parseDateFromString(const QString& dateString);
};

#endif
