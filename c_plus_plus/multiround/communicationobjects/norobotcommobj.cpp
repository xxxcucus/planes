#include "norobotcommobj.h"

#include <QMessageBox>
#include "viewmodels/norobotviewmodel.h"

bool NoRobotCommObj::makeRequest(const QString& requestId, const QString& answer)
{
    NoRobotViewModel requestData;
    requestData.m_requestId = requestId;
    requestData.m_answer = answer;
    
    m_RequestData = requestData.toJson();
    
    makeRequestBasis(false);
    return true;
}

void NoRobotCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    BasisCommObj::errorRequest(code);
    emit registrationFailed();
}

void NoRobotCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    QString username = retJson.value("username").toString();
    long int userid = retJson.value("id").toString().toLong(); //TODO check for convertion errors
    QMessageBox msgBox;
    msgBox.setText("User " + username + " created "); 
    msgBox.exec();

    emit registrationComplete();
}

bool NoRobotCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("username") && reply.contains("createdAt") && reply.contains("status"));
}
