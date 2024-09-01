#include "communicationtools.h"

#include <QJsonDocument>
#include <QMessageBox>
#include <QTimeZone>


//QString CommunicationTools::localTestServerPath = "https://planes.planes-android.com:8443/planesserver";
QString CommunicationTools::localTestServerPath = "http://localhost:8080/";

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
    QString registrationReplyQString(replyBA);
    
    QMessageBox msgBox(parentWidget);
    msgBox.setText("Error when " + actionName + " " + reply->errorString() + "\n" +  registrationReplyQString); 
    msgBox.exec();
}

/**
 * Parse QDateTime from format dd mm yyyy hh:mm:ss
 */
QDateTime CommunicationTools::parseDateFromString(const QString& dateString) {
        //qDebug() << "parseDateFromString " << dateString;
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
        return QDateTime(retDate, retTime, QTimeZone(QTimeZone::UTC));
    }
