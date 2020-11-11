#include "communicationtools.h"

#include <QJsonDocument>

QString CommunicationTools::localTestServerPath = "http://localhost:8080";

QNetworkReply * CommunicationTools::buildPostRequest(const QString& routePath, const QString& serverPath, const QJsonObject& jsonObject, QNetworkAccessManager* networkManager)
{
    QString requestPath = serverPath;
    if (serverPath.isEmpty())
        requestPath = localTestServerPath;
    qDebug() << "request to " << requestPath ;
    QUrl loginRequestUrl = QUrl(requestPath + routePath); 
    
    QNetworkRequest request(loginRequestUrl);
    request.setRawHeader("Content-Type", "application/fhir+json");
    
    qDebug() << "prepare request" ;
    QByteArray data = QJsonDocument(jsonObject).toJson();
    qDebug() << "prepare json";
    return networkManager->post(request, data);     
}

QJsonObject CommunicationTools::objectFromString(const QString& in)
{
    QJsonObject obj;
    QJsonDocument doc = QJsonDocument::fromJson(in.toUtf8());

    // check validity of the document
    if(!doc.isNull()) {
        if(doc.isObject()) {
            obj = doc.object();        
        } else {
            qDebug() << "Document is not an object";
        }
    } else {
        qDebug() << "Invalid JSON...\n" << in;
    }

    return obj;
}

