#include "norobotcommobj.h"

#include <QTimer>


bool NoRobotCommObj::makeRequest(const QString& requestId, const QString& answer)
{
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    m_RequestData = prepareViewModel(requestId, answer).toJson();
    emit logMessage("Connecting to server ..");

    makeRequestBasis(false);
    return true;
}

NoRobotViewModel NoRobotCommObj::prepareViewModel(const QString& requestId, const QString& answer) {
    NoRobotViewModel requestData;
    requestData.m_requestId = requestId;
    requestData.m_answer = answer;
    return requestData;
}

void NoRobotCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    BasisCommObj::errorRequest(code);
    emit registrationFailed();
    //qDebug() << "registration failed";
}

void NoRobotCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    processResponse(retJson);
}

void NoRobotCommObj::processResponse(const QJsonObject& retJson) {
    QString username = retJson.value("username").toString();
    //long int userid = retJson.value("id").toString().toLong();
    emit logMessage("User " + username + " created ");
    emit registrationComplete();
    //qDebug() << "registration completed";
}

bool NoRobotCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("id") && reply.contains("username") && reply.contains("createdAt") && reply.contains("status")))
        return false;
    
    if (!checkLong(reply.value("id").toString()))
        return false;
      
    return true;
}
