#include "norobotcommobj.h"

#include <QMessageBox>
#include "viewmodels/norobotviewmodel.h"

NoRobotCommObj::~NoRobotCommObj()
{
    delete m_LoadingMessageBox;
}


bool NoRobotCommObj::makeRequest(const QString& requestId, const QString& answer)
{
    NoRobotViewModel requestData;
    requestData.m_requestId = requestId;
    requestData.m_answer = answer;
    
    m_RequestData = requestData.toJson();
    
    m_LoadingMessageBox->show();

    makeRequestBasis(false);
    return true;
}

void NoRobotCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    if (m_LoadingMessageBox->isVisible())
        m_LoadingMessageBox->hide();

    BasisCommObj::errorRequest(code);
    emit registrationFailed();
}

void NoRobotCommObj::finishedRequest()
{
    if (m_LoadingMessageBox->isVisible())
        m_LoadingMessageBox->hide();

    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    QString username = retJson.value("username").toString();
    long int userid = retJson.value("id").toString().toLong();
    QMessageBox msgBox(m_ParentWidget);
    msgBox.setText("User " + username + " created "); 
    msgBox.exec();

    emit registrationComplete();
}

bool NoRobotCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("id") && reply.contains("username") && reply.contains("createdAt") && reply.contains("status")))
        return false;
    
    if (!checkLong(reply.value("id").toString()))
        return false;
      
    return true;
}
