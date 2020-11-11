#ifndef _COMMUNICATION_TOOLS__
#define _COMMUNICATION_TOOLS__

#include <QString>
#include <QNetworkReply>
#include <QJsonObject>

class CommunicationTools {

private:
    static QString localTestServerPath;

public:
    static QNetworkReply* buildPostRequest(const QString& routePath, const QString& serverPath, const QJsonObject& jsonObject, QNetworkAccessManager* networkManager);
    static QJsonObject objectFromString(const QString& in);
    
    
};

#endif
