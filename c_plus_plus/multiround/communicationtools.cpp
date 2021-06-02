#include "communicationtools.h"

#include <QJsonDocument>
#include <QMessageBox>
#include <QTextCodec>

QString CommunicationTools::localTestServerPath = "http://localhost:8080";

QNetworkReply * CommunicationTools::buildPostRequestWithAuth(const QString& routePath, const QString& serverPath, const QJsonObject& jsonObject, const QByteArray& authToken, QNetworkAccessManager* networkManager)
{
    QString requestPath = serverPath;
    if (serverPath.isEmpty())
        requestPath = localTestServerPath;
    //qDebug() << "request to " << requestPath ;
    QUrl loginRequestUrl = QUrl(requestPath + routePath); 
    
    QNetworkRequest request(loginRequestUrl);
    request.setRawHeader("Content-Type", "application/fhir+json");
    request.setRawHeader(QByteArray("Authorization"), authToken);
    QSslConfiguration config = QSslConfiguration::defaultConfiguration();
    config.setProtocol(QSsl::TlsV1_3);
    request.setSslConfiguration(config);
    
    //qDebug() << "prepare request" ;
    QByteArray data = QJsonDocument(jsonObject).toJson();
    //qDebug() << "prepare json";
    return networkManager->post(request, data);     
}

QNetworkReply * CommunicationTools::buildPostRequest(const QString& routePath, const QString& serverPath, const QJsonObject& jsonObject, QNetworkAccessManager* networkManager)
{
    QString requestPath = serverPath;
    if (serverPath.isEmpty())
        requestPath = localTestServerPath;
    //qDebug() << "request to " << requestPath ;
    QUrl loginRequestUrl = QUrl(requestPath + routePath); 
    
    QNetworkRequest request(loginRequestUrl);
    request.setRawHeader("Content-Type", "application/fhir+json");
    QSslConfiguration config = QSslConfiguration::defaultConfiguration();
    config.setProtocol(QSsl::TlsV1_3);
    request.setSslConfiguration(config);
    
    //qDebug() << "prepare request" ;
    QByteArray data = QJsonDocument(jsonObject).toJson();
    //qDebug() << "prepare json";
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
            //qDebug() << "Document is not an object";
        }
    } else {
        //qDebug() << "Invalid JSON...\n" << in;
    }

    return obj;
}

void CommunicationTools::treatCommunicationError(const QString& actionName, QNetworkReply* reply, QWidget* parentWidget) {
    QByteArray replyBA = reply->readAll();
    QString registrationReplyQString = QTextCodec::codecForMib(106)->toUnicode(replyBA);
    
    QMessageBox msgBox(parentWidget);
    msgBox.setText("Error when " + actionName + " " + reply->errorString() + "\n" +  registrationReplyQString); 
    msgBox.exec();
}
